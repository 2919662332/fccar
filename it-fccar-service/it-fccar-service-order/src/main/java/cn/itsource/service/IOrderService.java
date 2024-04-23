package cn.itsource.service;

import cn.itsource.dto.LocalParams;
import cn.itsource.pojo.bo.CustomerOrderResp;
import cn.itsource.pojo.bo.OrderBillResp;
import cn.itsource.pojo.domain.Order;
import cn.itsource.pojo.dto.EnterFeeParams;
import cn.itsource.pojo.dto.LoadPayOrderDto;
import cn.itsource.pojo.dto.OrderParamDto;
import cn.itsource.pojo.vo.DriverKillOrderDataResp;
import cn.itsource.pojo.vo.LocalInfoResp;
import cn.itsource.vo.OrderRespVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author ????
 * @since 2024-04-10
 */
public interface IOrderService extends IService<Order> {

    String createOrder(OrderParamDto paramDto);

    List<OrderRespVo> pullOrder();

    void autoCancelOrder();

    void handCancelOrder();

    void killOrder(String orderNo);

    Boolean selectOrderStatus(String orderNo);

    DriverKillOrderDataResp selectOrderStatusData();

    void arriveLocal(String orderNo);

    LocalInfoResp localInfo(String orderNo);

    LocalParams pullDriverLocal(String orderNo);

    LocalInfoResp customerLocation(String orderNo);

    CustomerOrderResp loadCustomerOrder();

    void setDriveStatus(String orderNo);

    void endDriver(String orderNo);

    void confirm(EnterFeeParams params);

    OrderBillResp getOrderbill(String orderNo);

    void sendPayOrder(String orderNo);

    LoadPayOrderDto loadPayOrder(String orderNo);
}
