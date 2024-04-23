package cn.itsource.service;

import cn.itsource.pojo.domain.CustomerWallet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 乘客的钱包 服务类
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
public interface ICustomerWalletService extends IService<CustomerWallet> {

    void saveWallet(Long id);
}
