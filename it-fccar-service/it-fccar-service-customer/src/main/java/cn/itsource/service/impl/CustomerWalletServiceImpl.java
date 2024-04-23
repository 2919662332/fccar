package cn.itsource.service.impl;

import cn.itsource.pojo.domain.CustomerWallet;
import cn.itsource.mapper.CustomerWalletMapper;
import cn.itsource.service.ICustomerWalletService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 乘客的钱包 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
@Service
public class CustomerWalletServiceImpl extends ServiceImpl<CustomerWalletMapper, CustomerWallet> implements ICustomerWalletService {

    @Override
    public void saveWallet(Long id) {
        CustomerWallet customerWallet = new CustomerWallet();
        customerWallet.setId(id);
        customerWallet.setCreateTime(new Date());
        super.save(customerWallet);
    }
}
