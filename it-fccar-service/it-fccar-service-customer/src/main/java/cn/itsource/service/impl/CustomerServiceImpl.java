package cn.itsource.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.itsource.constants.Constants;
import cn.itsource.constants.GlobalExceptionCode;
import cn.itsource.exception.GlobalException;
import cn.itsource.pojo.domain.Customer;
import cn.itsource.mapper.CustomerMapper;
import cn.itsource.pojo.dto.WxCodeDto;
import cn.itsource.remote.api.FeignApi;
import cn.itsource.remote.pojo.SaveLoginParam;
import cn.itsource.result.R;
import cn.itsource.service.ICustomerService;
import cn.itsource.service.ICustomerSummaryService;
import cn.itsource.service.ICustomerWalletFlowService;
import cn.itsource.service.ICustomerWalletService;
import cn.itsource.template.WechatLoginTemplate;
import cn.itsource.utils.BitStatesUtil;
import cn.itsource.utils.NameUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 客户 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Resource
    private WechatLoginTemplate wechatLoginTemplate;
    @Resource
    private ICustomerSummaryService summaryService;
    @Resource
    private ICustomerWalletFlowService walletFlowService;
    @Resource
    private ICustomerWalletService walletService;
    @Resource
    private FeignApi feignApi;

    @Override
    @GlobalTransactional(name = "customer-wechat-register",rollbackFor = Exception.class)
    public R register(WxCodeDto wxCodeDto) {
        String wxOpenId = wechatLoginTemplate.getWxOpenId(wxCodeDto.getWxcode());
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Customer> eq = wrapper.eq(Customer::getOpenId, wxOpenId);
        Customer customer = super.getOne(eq);
        if (customer != null) {
            throw new GlobalException(GlobalExceptionCode.WECHAT_REGISTER_ERROR);
        }
        Customer customers = new Customer();
        //雪花算法生产id
        Long id = IdUtil.getSnowflake(1, 5).nextId();
        //写死的手机号
        String phone = "1008611";

        //获取手机号码
//        String phoneNumber = wechatLoginTemplate.getPhoneNumber(wxCodeDto.getPhoneCode());


        //写死的邮箱
        String email = "2919662332@qq.com";
        customers.setOpenId(wxOpenId);
        customers.setId(id);
        customers.setBitState(BitStatesUtil.addState(0L,BitStatesUtil.OP_PHONE));
        customers.setPhone(phone);
        customers.setEmail(email);
        customers.setCreateTime(new Date());
        customers.setName(NameUtil.getName());
        super.save(customers);

        //保存乘客钱包
        walletService.saveWallet(id);
        //保存乘客小计
        summaryService.saveSummary(id);
        //保存钱包流水
        walletFlowService.saveWalletFlow(id);

        //保存乘客登录信息
        SaveLoginParam saveLoginParam = new SaveLoginParam();
        saveLoginParam.setId(id);
        saveLoginParam.setUsername(phone);
        saveLoginParam.setType(Constants.Login.TYPE_CUSTOMER);
        saveLoginParam.setCreateTime(new Date());
        saveLoginParam.setPhone(phone);
        saveLoginParam.setOpenId(wxOpenId);
        R r = feignApi.saveLogin(saveLoginParam);
        if (!r.isSuccess()){
            throw new GlobalException("保存失败！");
        }
        return R.success();

    }
}
