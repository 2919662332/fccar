package cn.itsource.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.itsource.bo.DriverGeo;
import cn.itsource.constants.Constants;
import cn.itsource.constants.GlobalExceptionCode;
import cn.itsource.dto.DriverSettingDto;
import cn.itsource.exception.GlobalException;
import cn.itsource.pojo.domain.Driver;
import cn.itsource.mapper.DriverMapper;
import cn.itsource.pojo.domain.DriverSetting;
import cn.itsource.pojo.domain.DriverSummary;
import cn.itsource.dto.LocalParams;
import cn.itsource.pojo.dto.RealAuthStatus;
import cn.itsource.pojo.dto.RegisterDto;
import cn.itsource.remote.api.FeignApi;
import cn.itsource.remote.pojo.SaveLoginParam;
import cn.itsource.result.R;
import cn.itsource.service.*;
import cn.itsource.template.WechatLoginTemplate;
import cn.itsource.utils.AssertUtil;
import cn.itsource.utils.BitStatesUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 司机对象 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
@Service
public class DriverServiceImpl extends ServiceImpl<DriverMapper, Driver> implements IDriverService {
    @Resource
    private IDriverSettingService settingService;
    @Resource
    private FeignApi feignApi;
    @Resource
    private IDriverWalletService walletService;
    @Resource
    private IDriverSummaryService summaryService;
    @Resource
    private IDriverSettingService driverSettingService;
    @Resource
    private WechatLoginTemplate wechatLoginTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 保存司机信息
     *
     * @param registerDto 注册信息DTO
     * @return 保存后的司机对象
     * @throws GlobalException 如果该微信号已注册，则抛出异常
     */
    @Override
    @GlobalTransactional(name = "driver-wechat-register",rollbackFor = Exception.class)
    public Driver save(RegisterDto registerDto) throws JsonProcessingException {
        // 获取微信码
        String wxCode = registerDto.getWxCode();
        // 通过微信码获取微信OpenID
        String wxOpenId = wechatLoginTemplate.getWxOpenId(wxCode);
        //将openid保存到redis分账计算要使用
        String format = String.format(Constants.Redis.DRIVER_OPENID, StpUtil.getLoginIdAsLong());
        redisTemplate.opsForValue().set(format,wxOpenId);
        // 检查是否已存在该OpenID的司机
        Driver driver = checkIfDriverExists(wxOpenId);
        AssertUtil.isNull(driver,GlobalExceptionCode.WECHAT_REGISTER_ERROR);
        // 创建新的司机对象
        Driver newDriver = createNewDriver(wxOpenId,registerDto);
        // 保存司机设置
        saveDriverSettings(newDriver.getId());
        // 保存司机钱包信息
        saveDriverWallet(newDriver.getId());
        // 保存司机概要信息
        saveDriverSummary(newDriver.getId());
        //调用feign保存登陆信息
        saveLoginInfo(newDriver.getId(),wxOpenId, newDriver.getPhone());
        // 返回新创建的司机对象
        return newDriver;
    }

    @Override
    public RealAuthStatus getRealAuthSuccess() {
        RealAuthStatus realAuthStatus = new RealAuthStatus();
        Driver driver = super.getById(StpUtil.getLoginIdAsLong());

        realAuthStatus.setBitStatus(driver.getBitState());

        DriverSummary driverSummary = summaryService.getById(StpUtil.getLoginIdAsLong());
        realAuthStatus.setRealAuthSuccess(BitStatesUtil.hasState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATIONED));
        realAuthStatus.setDriveDuration(driverSummary.getDriveDuration());
        realAuthStatus.setTodayIncome(driverSummary.getTodayIncome());
        realAuthStatus.setTodayTradeOrders(driverSummary.getTodayTradeOrders());
        return realAuthStatus;
    }

    /**
     * 保存司机的配置信息到redis
     * @return
     */
    @Override
    public R<DriverSettingDto> saveSetting() {
        DriverSettingDto driverSettingDtos = new DriverSettingDto();
        DriverSetting driverSetting = driverSettingService.getById(StpUtil.getLoginIdAsLong());
        driverSettingDtos.setOrderDistance(driverSetting.getOrderDistance());
        driverSettingDtos.setRangeDistance(driverSetting.getRangeDistance());
        String key = String.format(Constants.Redis.DRIVER_SETTING_KEY, StpUtil.getLoginIdAsLong());
        redisTemplate.opsForValue().set(key,driverSettingDtos);
        return R.success();
    }

    /**
     * 将当前位置通过redis的geo保存在redis中
     * @param driverGeo
     */
    @Override
    public void saveGeo(DriverGeo driverGeo) {
        //将driverGeo中的经纬度保存到Point中
        Point point = new Point(driverGeo.getLongitude(), driverGeo.getLatitude());
        redisTemplate.opsForGeo().add(Constants.Redis.DRIVER_LOCATION_KEY,point,StpUtil.getLoginIdAsLong());
    }

    /**
     * app停止接单删除redis中的位置数据
     */
    @Override
    public void stopAcceptOrder() {
        //从redis的geo中删除保存的位置数据
        redisTemplate.opsForGeo().remove(Constants.Redis.DRIVER_LOCATION_KEY,StpUtil.getLoginIdAsLong());
    }

    /**
     * 将司机坐标信息缓存到redis，用于乘客读取位置信息做司乘同显
     * @param localParams
     */
    @Override
    public void location(LocalParams localParams) {
        String format = String.format(Constants.Redis.DRIVER_LOCATION_TO_REDIS_KEY, localParams.getOrderNo());
        redisTemplate.opsForValue().set(format,localParams);
    }


    /**
     * 检查是否已存在指定OpenID的司机
     *
     * @param wxOpenId 微信OpenID
     * @return 如果存在则返回司机对象，否则返回null
     */
    private Driver checkIfDriverExists(String wxOpenId) {
        // 创建Lambda查询包装器
        LambdaQueryWrapper<Driver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 根据OpenID查询司机对象
        return super.getOne(lambdaQueryWrapper.eq(Driver::getOpenId, wxOpenId));
    }

    /**
     * 创建新的司机对象
     *
     * @param wxOpenId 微信OpenID
     * @return 新创建的司机对象
     */
    private Driver createNewDriver(String wxOpenId,RegisterDto registerDto) throws JsonProcessingException {
        // 创建雪花ID生成器
        Snowflake snowflake = IdUtil.createSnowflake(1, 5);
        // 生成司机ID
        long id = snowflake.nextId();
        //获取司机手机号
//        String phone = wechatLoginTemplate.getPhoneNumber(registerDto.getPhoneCode());
        String phone = "123456";
        // 创建司机对象
        Driver drivers = new Driver();
        // 设置司机信息
        driverInfo(drivers, wxOpenId, id, phone);
        // 返回司机对象
        return drivers;
    }

    /**
     * 设置司机信息
     *
     * @param drivers  司机对象
     * @param openid   微信OpenID
     * @param id       司机ID
     */
    private void driverInfo(Driver drivers, String openid, Long id,String phone) {
        // 设置司机ID
        drivers.setId(id);
        // 设置微信OpenID
        drivers.setOpenId(openid);
        // 假设电话号码为固定值
        drivers.setPhone(phone);
        // 设置司机状态位
        drivers.setBitState(BitStatesUtil.addState(0L, BitStatesUtil.OP_PHONE));
        // 设置创建时间
        drivers.setCreateTime(new Date());
        // 保存司机信息
        super.save(drivers);
    }

    /**
     * 保存司机设置信息
     *
     * @param driverId 司机ID
     */
    private void saveDriverSettings(Long driverId) {
        // 调用设置服务保存司机设置信息
        settingService.save(driverId);
    }

    private void saveDriverWallet(Long driverId) {
        walletService.save(driverId);
    }

    private void saveDriverSummary(Long driverId) {
        summaryService.save(driverId);
    }

    //通过feign调用保存登陆信息
    private void saveLoginInfo(Long driverId,String openId,String phone) {
        SaveLoginParam saveLoginParam = new SaveLoginParam();
        saveLoginParam.setId(driverId);
        saveLoginParam.setUsername(phone);
        saveLoginParam.setType(Constants.Login.TYPE_DRIVER);
        saveLoginParam.setPhone(phone);
        saveLoginParam.setOpenId(openId);
        R r = feignApi.saveLogin(saveLoginParam);
        if (!r.isSuccess()){
            throw new GlobalException("保存失败！");
        }
    }
}
