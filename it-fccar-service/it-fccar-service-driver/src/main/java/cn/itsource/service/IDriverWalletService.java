package cn.itsource.service;

import cn.itsource.pojo.domain.DriverWallet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 司机的钱包 服务类
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
public interface IDriverWalletService extends IService<DriverWallet> {

    void save(long id);
}
