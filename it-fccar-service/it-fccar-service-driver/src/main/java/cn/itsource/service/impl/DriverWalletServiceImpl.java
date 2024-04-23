package cn.itsource.service.impl;

import cn.itsource.pojo.domain.DriverWallet;
import cn.itsource.mapper.DriverWalletMapper;
import cn.itsource.service.IDriverWalletService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 司机的钱包 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
@Service
public class DriverWalletServiceImpl extends ServiceImpl<DriverWalletMapper, DriverWallet> implements IDriverWalletService {

    @Override
    public void save(long id) {
        DriverWallet driverWallet = new DriverWallet();
        driverWallet.setId(id);
        driverWallet.setAmount(new BigDecimal("0.00"));
        driverWallet.setCreateTime(new Date());
        super.save(driverWallet);
    }
}
