package cn.itsource.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.itsource.constants.Constants;
import cn.itsource.exception.GlobalException;
import cn.itsource.mapper.DriverMapper;
import cn.itsource.mapper.DriverMaterialAuthLogMapper;
import cn.itsource.pojo.domain.Driver;
import cn.itsource.pojo.domain.DriverAuthMaterial;
import cn.itsource.mapper.DriverAuthMaterialMapper;
import cn.itsource.pojo.domain.DriverMaterialAuthLog;
import cn.itsource.pojo.dto.MaterialDto;
import cn.itsource.remote.api.FeignApi;
import cn.itsource.remote.pojo.SaveLoginName;
import cn.itsource.result.R;
import cn.itsource.service.IDriverAuthMaterialService;
import cn.itsource.template.AuthTemplate;
import cn.itsource.utils.BitStatesUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 司机实名资料 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
@Service
@Slf4j
public class DriverAuthMaterialServiceImpl extends ServiceImpl<DriverAuthMaterialMapper, DriverAuthMaterial> implements IDriverAuthMaterialService {

    @Resource
    private DriverMapper driverMapper;

    @Resource
    private FeignApi feignApi;

    @Resource
    private AuthTemplate authTemplate;

    @Resource
    private DriverMaterialAuthLogMapper materialAuthLogMapper;

    /**
     * 司机提交实名材料
     * @param authMaterial
     * @return
     */
    @Override
    @GlobalTransactional
    public R<DriverAuthMaterial> submitAuthMaterials(DriverAuthMaterial authMaterial) {
        if (authMaterial.getIdNumber() == null || authMaterial.getName() == null) {
            throw new GlobalException("参数不能为空！");
        }
        //身份证二要素核验
//        if (!authTemplate.AuthStatus(authMaterial.getName(), authMaterial.getIdNumber())) {
//            throw new GlobalException("身份证核验失败！");
//        }
        //获取登录的id
        long loginId = StpUtil.getLoginIdAsLong();
        //查询实名材料对应的司机对象
        Driver driver = driverMapper.selectById(loginId);
        if (BitStatesUtil.hasState(driver.getBitState(), BitStatesUtil.OP_REAL_AUTHENTICATING)) {
            throw new GlobalException("实名认证中，请勿重复提交！");
        }
        if (BitStatesUtil.hasState(driver.getBitState(), BitStatesUtil.OP_REAL_AUTHENTICATIONED)) {
            throw new GlobalException("实名已经完成！");
        }
        DriverAuthMaterial dbAuthMaterial = super.getById(loginId);
        if (dbAuthMaterial == null) {
            DriverAuthMaterial AuthMaterial = new DriverAuthMaterial();
            Date date = new Date();
            BeanUtils.copyProperties(authMaterial, AuthMaterial);
            AuthMaterial.setCreateTime(date);
            AuthMaterial.setDriverId(loginId);
            AuthMaterial.setId(loginId);
            //设置注册中的位状态
            AuthMaterial.setRealAuthStatus(Constants.RealAuth.VERIFYING);
            //添加司机的实名位状态
            driver.setUpdateTime(date);
            driver.setBitState(BitStatesUtil.addState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATING));
            //插入实名材料数据
            super.save(AuthMaterial);
            //更新司机数据
            driverMapper.updateById(driver);
            return R.success(AuthMaterial);
        }else if(dbAuthMaterial != null && (Objects.equals(dbAuthMaterial.getRealAuthStatus(), Constants.RealAuth.REVOKE))){
            //重新提交了实名材料
            dbAuthMaterial.setRealAuthStatus(Constants.RealAuth.VERIFYING);
            driver.setBitState(BitStatesUtil.addState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATING));
            //更新实名材料数据
            super.updateById(dbAuthMaterial);
            //更新司机数据
            driverMapper.updateById(driver);
            //再去数据库查询新状态返回
            DriverAuthMaterial RedbAuthMaterial01 = super.getById(loginId);
            return R.success(RedbAuthMaterial01);
        } else if(dbAuthMaterial != null && (Objects.equals(dbAuthMaterial.getRealAuthStatus(), Constants.RealAuth.VERIFY_FAIL))){
            //重新提交了实名材料
            dbAuthMaterial.setRealAuthStatus(Constants.RealAuth.VERIFYING);
            driver.setBitState(BitStatesUtil.addState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATING));
            //更新实名材料数据
            super.updateById(dbAuthMaterial);
            //更新司机数据
            driverMapper.updateById(driver);
            //再去数据库查询新状态返回
            DriverAuthMaterial RedbAuthMaterial02 = super.getById(loginId);
            return R.success(RedbAuthMaterial02);
        }else {
            //实名材料已经提交过了
            throw new GlobalException("实名已经提交过了！");
        }
    }

    @Override
    public R<DriverAuthMaterial> queryEchoData() {
        long loginId = StpUtil.getLoginIdAsLong();
        DriverAuthMaterial authMaterial = super.getById(loginId);
        DriverAuthMaterial driverAuthMaterial = new DriverAuthMaterial();
        if (authMaterial!= null) {
            return R.success(authMaterial);
        }
        return R.success(driverAuthMaterial);
    }

    /**
     * 管理前台审核实名材料
     * @param materialDto
     * @return
     */
    @Override
    @GlobalTransactional
    public R auditMaterial(MaterialDto materialDto) {
        if (materialDto.getId() == null ||materialDto.getApprove() == null || materialDto.getRemark() == null ||materialDto == null){
            throw new GlobalException("参数错误！");
        }
        //实名材料审核
        //*1.判断:参数判断，条件判断
        Driver driver = driverMapper.selectById(materialDto.getId());
        if (BitStatesUtil.hasState(driver.getBitState(), BitStatesUtil.OP_REAL_AUTHENTICATIONED)) {
            throw new GlobalException("已完成实名，无需重复实名！");
        }
        //根据前端id获取数据库材料
        DriverAuthMaterial authMat = super.getById(materialDto.getId());
        Date date = new Date();
        if (materialDto.getApprove()){
            //将材料的认证状态改为完成
            authMat.setRealAuthStatus(Constants.RealAuth.APPROVED);
            //移除实名认证中的位状态
            driver.setBitState(BitStatesUtil.removeState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATING));
            //设置备注
            authMat.setAuditRemark(materialDto.getRemark());
            //设置审核时间
            authMat.setAuditTime(date);
            //设置审核人id
            authMat.setAuditUserId(StpUtil.getLoginIdAsLong());
            //设置更新时间
            authMat.setUpdateTime(date);
            //修改driver位状态
            driver.setBitState(BitStatesUtil.addState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATIONED));
            //设置备注
            authMat.setAuditRemark(materialDto.getRemark());
            //更新材料状态
            super.updateById(authMat);
            //更新司机位状态
            driverMapper.updateById(driver);
            //添加审核表
            insertAuthLog(authMat);
            //修改driver位状态
            driver.setBitState(BitStatesUtil.addState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATIONED));
            //设置备注
            authMat.setAuditRemark(materialDto.getRemark());
            //更新材料状态
            super.updateById(authMat);
            //更新司机位状态
            driverMapper.updateById(driver);
            //添加审核表
            insertAuthLog(authMat);

            //调用feign保存登陆人姓名
            SaveLoginName saveLoginName = new SaveLoginName();
            saveLoginName.setId(authMat.getId());
            saveLoginName.setName(authMat.getName());
            feignApi.saveLoginName(saveLoginName);
            /**
             * 远程调用feign保存登录人的名字
             * 拒绝审核材料
             */
        }else {
            //移除实名认证中的位状态
            driver.setBitState(BitStatesUtil.removeState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATING));
            //设置备注
            authMat.setAuditRemark(materialDto.getRemark());
            //设置审核时间
            authMat.setAuditTime(date);
            //设置更新时间
            authMat.setUpdateTime(date);
            //设置审核人id
            authMat.setAuditUserId(StpUtil.getLoginIdAsLong());
            //设置材料的状态为失败
            authMat.setRealAuthStatus(Constants.RealAuth.VERIFY_FAIL);
            //更新材料表
            super.updateById(authMat);
            //插入材料日志
            insertAuthLog(authMat);
        }
        return R.success();
    }

    /**
     * 撤销实名认证
     * @param materialDto
     * @return
     */
    @Override
    @GlobalTransactional
    public R cancelAuditMaterial(MaterialDto materialDto) {
        if (materialDto.getId() == null ||materialDto.getApprove() == null || materialDto.getRemark() == null ||materialDto == null){
            throw new GlobalException("参数错误！");
        }
        //获取driver参数
        Driver driver = driverMapper.selectById(materialDto.getId());
        //根据前端id获取数据库材料
        DriverAuthMaterial authMat = super.getById(materialDto.getId());
        Date date = new Date();
        if (materialDto.getApprove()){
            //移除实名认证中的位状态
            driver.setBitState(BitStatesUtil.removeState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATIONED));
            //设置备注
            authMat.setAuditRemark(materialDto.getRemark());
            //设置审核时间
            authMat.setAuditTime(date);
            //设置更新时间
            authMat.setUpdateTime(date);
            //设置审核人id
            authMat.setAuditUserId(StpUtil.getLoginIdAsLong());
            //设置材料的状态为失败
            authMat.setRealAuthStatus(Constants.RealAuth.REVOKE);
            //更新材料表
            super.updateById(authMat);
            //更新司机表
            driverMapper.updateById(driver);
            //插入材料日志
            insertAuthLog(authMat);
        }
        return R.success();
    }


    //添加审核日志
    private void insertAuthLog(DriverAuthMaterial authMeatialLog){
        DriverMaterialAuthLog driverMaterialAuthLog = new DriverMaterialAuthLog();
        driverMaterialAuthLog.setAuthMaterialId(authMeatialLog.getId());
        driverMaterialAuthLog.setRealAuthStatus(authMeatialLog.getRealAuthStatus());
        driverMaterialAuthLog.setAuditTime(authMeatialLog.getUpdateTime());
        driverMaterialAuthLog.setAuditUserId(authMeatialLog.getAuditUserId());
        driverMaterialAuthLog.setAuditRemark(authMeatialLog.getAuditRemark());
        driverMaterialAuthLog.setCreateTime(new Date());
        materialAuthLogMapper.insert(driverMaterialAuthLog);
    }
}
