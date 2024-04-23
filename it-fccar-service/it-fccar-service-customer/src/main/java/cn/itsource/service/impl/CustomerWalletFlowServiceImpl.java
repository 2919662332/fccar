package cn.itsource.service.impl;

import cn.itsource.constants.Constants;
import cn.itsource.pojo.domain.CustomerWalletFlow;
import cn.itsource.mapper.CustomerWalletFlowMapper;
import cn.itsource.service.ICustomerWalletFlowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 钱包流水 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
@Service
public class CustomerWalletFlowServiceImpl extends ServiceImpl<CustomerWalletFlowMapper, CustomerWalletFlow> implements ICustomerWalletFlowService {

    @Override
    public void saveWalletFlow(Long id) {
        CustomerWalletFlow customerWalletFlow = new CustomerWalletFlow();
        customerWalletFlow.setId(id);
        customerWalletFlow.setCreateTime(new Date());
        customerWalletFlow.setWalletId(id);
        customerWalletFlow.setAmount(new BigDecimal("0.00"));
        customerWalletFlow.setFlowType(1);
        customerWalletFlow.setIncome(true);
        customerWalletFlow.setWalletAmount(new BigDecimal("0.00"));
        super.save(customerWalletFlow);
    }
}
