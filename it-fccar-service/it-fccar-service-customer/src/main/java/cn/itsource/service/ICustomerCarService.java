package cn.itsource.service;

import cn.itsource.pojo.domain.CustomerCar;
import cn.itsource.result.R;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户车辆 服务类
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
public interface ICustomerCarService extends IService<CustomerCar> {

    List<CustomerCar> selectCar();

    R addCar(CustomerCar customerCar);
}
