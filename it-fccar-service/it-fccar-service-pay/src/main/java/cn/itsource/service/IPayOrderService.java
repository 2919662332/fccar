package cn.itsource.service;

import cn.itsource.bo.PayOrderData;
import cn.itsource.pojo.domain.PayOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ????
 * @since 2024-04-19
 */
public interface IPayOrderService extends IService<PayOrder> {

    PayOrder selectByorderNo(String orderNo);

    void create(PayOrderData payOrder);
}
