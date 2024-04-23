package cn.itsource.service;

import cn.itsource.pojo.domain.CustomerWalletFlow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 钱包流水 服务类
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
public interface ICustomerWalletFlowService extends IService<CustomerWalletFlow> {

    void saveWalletFlow(Long id);
}
