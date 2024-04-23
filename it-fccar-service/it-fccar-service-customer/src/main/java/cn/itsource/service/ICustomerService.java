package cn.itsource.service;

import cn.itsource.pojo.domain.Customer;
import cn.itsource.pojo.domain.CustomerCar;
import cn.itsource.pojo.dto.WxCodeDto;
import cn.itsource.result.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * <p>
 * 客户 服务类
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
public interface ICustomerService extends IService<Customer> {

    R register(WxCodeDto wxCodeDto) throws JsonProcessingException;

}
