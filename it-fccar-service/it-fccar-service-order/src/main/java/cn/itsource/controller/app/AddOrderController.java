package cn.itsource.controller.app;

import cn.itsource.dto.LocalParams;
import cn.itsource.pojo.bo.CustomerOrderResp;
import cn.itsource.pojo.bo.OrderBillResp;
import cn.itsource.pojo.dto.EnterFeeParams;
import cn.itsource.pojo.dto.LoadPayOrderDto;
import cn.itsource.pojo.dto.OrderParamDto;
import cn.itsource.pojo.vo.DriverKillOrderDataResp;
import cn.itsource.pojo.vo.LocalInfoResp;
import cn.itsource.result.R;
import cn.itsource.service.IOrderBillService;
import cn.itsource.service.IOrderService;
import cn.itsource.vo.OrderRespVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/app/order")
public class AddOrderController {
    @Resource
    private IOrderService orderService;
    @Resource
    IOrderBillService orderBillService;

    @GetMapping("/location/{orderNo}")
    public R<LocalInfoResp> CustomerLocation(@PathVariable String orderNo) {
        LocalInfoResp localInfoResp = orderService.customerLocation(orderNo);
        return R.success(localInfoResp);
    }

    /**
     * 司机发送账单乘客回显
     * @param orderNo
     * @return
     */
    @GetMapping("detail/customer/{orderNo}")
    public R<LoadPayOrderDto> loadPayOrder(@PathVariable String orderNo) {
        LoadPayOrderDto loadPayOrderDto = orderService.loadPayOrder(orderNo);
        return R.success(loadPayOrderDto);
    }
    @GetMapping("/orderbill/{orderNo}")
    public R<OrderBillResp> getOrderbill(@PathVariable String orderNo) {
        OrderBillResp orderBillResp = orderService.getOrderbill(orderNo);
        return R.success(orderBillResp);
    }
    @GetMapping("/endDriver/{orderNo}")
    public R<Boolean> endDriver(@PathVariable String orderNo) {
        orderService.endDriver(orderNo);
        return R.success();
    }

    /**
     * 司机开始代驾
     * @param orderNo
     * @return
     */
    @GetMapping("/startDrive/{orderNo}")
    public R<Boolean> startDrive(@PathVariable String orderNo) {
        orderService.setDriveStatus(orderNo);
        return R.success();
    }
    @GetMapping("/loadCustomerOrder")
    public R<CustomerOrderResp> loadCustomerOrder() {
        CustomerOrderResp customerOrderResp = orderService.loadCustomerOrder();
        return R.success(customerOrderResp);
    }

    @GetMapping("/pull/driver/location/{orderNo}")
    public R<LocalParams> pullDriverLocal(@PathVariable String orderNo) {
        LocalParams localInfoResp = orderService.pullDriverLocal(orderNo);
        return R.success(localInfoResp);
    }


    /**
     * 乘客创建订单
     *
     * @param paramDto 参数 DTO
     * @return {@link R}
     */
    @PostMapping("/createOrder")
    public R createOrder(@RequestBody @Valid OrderParamDto paramDto) {
        String orderNo = orderService.createOrder(paramDto);
        return R.success(orderNo);
    }
    @PostMapping("/sendPayOrder/{orderNo}")
    public R sendPayOrder(@PathVariable String orderNo) {
        orderService.sendPayOrder(orderNo);
        return R.success();
    }
    @PostMapping("/confirm")
    public R<Boolean> confirm(@RequestBody @Valid EnterFeeParams params) {
        orderService.confirm(params);
        return R.success();
    }
    @GetMapping("/localInfo/{orderNo}")
    public R<LocalInfoResp> localInfo(@PathVariable String orderNo) {
        LocalInfoResp  localInfoResp = orderService.localInfo(orderNo);
        return R.success(localInfoResp);
    }

    /**
     * 乘客端下单实时查询订单是否被接单
     * @param orderNo
     * @return
     */
    @PostMapping("/selectOrderStatus/{orderNo}")
    public R<Boolean> selectOrderStatus(@PathVariable String orderNo) {
        return R.success(orderService.selectOrderStatus(orderNo));
    }

    /**
     * 司机端回显订单的状态
     * @return
     */
    @PostMapping("/selectOrderStatusData")
    public R<DriverKillOrderDataResp> selectOrderStatusData() {
        DriverKillOrderDataResp respData = orderService.selectOrderStatusData();
        return R.success(respData);
    }
    @PostMapping("/arriveLocal/{orderNo}")
    public R<Boolean> arriveLocal(@PathVariable String orderNo) {
        orderService.arriveLocal(orderNo);
        return R.success();
    }

    /**
     * 司机端拉取订单
     * @return
     */
    @PostMapping("/pull")
    public R pullOrder() {
        List<OrderRespVo> list = orderService.pullOrder();
        return R.success(list);
    }

    /**
     * 自动取消订单
     *
     * @return {@link R}
     */
    @PostMapping("/autoCancelOrder")
    public R autoCancelOrder() {
        orderService.autoCancelOrder();
        return R.success();
    }

    /**
     * 手动取消订单
     *
     * @return {@link R}
     */
    @PostMapping("/handCancelOrder")
    public R handCancelOrder() {
        orderService.handCancelOrder();
        return R.success();
    }

    /**
     * 司机抢单
     *
     * @param orderNo 订货号
     * @return {@link R}
     */
    @PostMapping("/killOrder/{orderNo}")
    public R killOrder(@PathVariable String orderNo) {
        orderService.killOrder(orderNo);
        return R.success();
    }
}
