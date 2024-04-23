package cn.itsource.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.itsource.pojo.domain.CustomerCar;
import cn.itsource.mapper.CustomerCarMapper;
import cn.itsource.result.R;
import cn.itsource.service.ICustomerCarService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 客户车辆 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
@Service
public class CustomerCarServiceImpl extends ServiceImpl<CustomerCarMapper, CustomerCar> implements ICustomerCarService {

    @Override
    public List<CustomerCar> selectCar() {
        return super.list(new LambdaQueryWrapper<CustomerCar>().eq(CustomerCar::getCustomerId, StpUtil.getLoginIdAsLong()));
    }

    @Override
    public R addCar(CustomerCar customerCar) {
        //添加车辆信息
        customerCar.setCustomerId(StpUtil.getLoginIdAsLong());
        customerCar.setId(IdUtil.getSnowflake(1,1).nextId());
        customerCar.setCreateTime(new Date());
        super.save(customerCar);
        return R.success();
    }
}
