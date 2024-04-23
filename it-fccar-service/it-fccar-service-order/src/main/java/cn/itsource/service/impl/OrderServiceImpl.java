package cn.itsource.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.itsource.bo.DriverInfoBo;
import cn.itsource.bo.OrderBillBo;
import cn.itsource.bo.OrderProfitBo;
import cn.itsource.constants.Constants;
import cn.itsource.domian.GeoSearchResult;
import cn.itsource.domian.LoginSuccess;
import cn.itsource.dto.DriverPoint;
import cn.itsource.dto.DriverSettingDto;
import cn.itsource.dto.LocalParams;
import cn.itsource.exception.GlobalException;
import cn.itsource.mapper.OrderBillMapper;
import cn.itsource.pojo.bo.CustomerOrderResp;
import cn.itsource.pojo.bo.OrderBillResp;
import cn.itsource.bo.PayOrderData;
import cn.itsource.pojo.domain.Order;
import cn.itsource.mapper.OrderMapper;
import cn.itsource.pojo.domain.OrderBill;
import cn.itsource.pojo.domain.OrderProfitsharing;
import cn.itsource.pojo.dto.EnterFeeParams;
import cn.itsource.pojo.dto.LoadPayOrderDto;
import cn.itsource.pojo.dto.OrderParamDto;
import cn.itsource.pojo.vo.DriverKillOrderDataResp;
import cn.itsource.pojo.vo.LocalInfoResp;
import cn.itsource.remote.api.BigFeignApi;
import cn.itsource.remote.api.DriverFeignApi;
import cn.itsource.remote.api.RuleFeignApi;
import cn.itsource.remote.pojo.dto.ParamsDto;
import cn.itsource.remote.pojo.dto.PricingParametersDto;
import cn.itsource.remote.pojo.dto.SetOrderParam;
import cn.itsource.result.R;
import cn.itsource.service.IOrderService;
import cn.itsource.template.BigDataTemplate;
import cn.itsource.vo.OrderRespVo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.PullStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-04-10
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private static BigDecimal REAL_MEAILE = BigDecimal.ZERO;
    @Resource
    private DefaultMQPullConsumer pullConsumer;
    @Resource
    private RuleFeignApi ruleFeignApi;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderBillMapper orderBillMapper;
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private BigFeignApi bigFeignApi;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private BigDataTemplate bigDataTemplate;

    @Resource
    private OrderProfitsharingServiceImpl orderProfitsharingService;

    @Resource
    private DriverFeignApi driverFeignApi;
    //管理偏移量的集合
    private static final Map<MessageQueue,Long> OFFECT_TABLE = new HashMap<>();

    /**
     * 查询司机抢单后回显的数据
     * @return
     */
    @Override
    public DriverKillOrderDataResp selectOrderStatusData() {
        Order one = super.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getDriverId,StpUtil.getLoginIdAsLong())
                .in(Order::getStatus,Constants.Order.DRIVER_ORDER_IN_PROGRESS));
        if (one == null){
            return null;
        }
        DriverKillOrderDataResp driverKillOrderDataResp = new DriverKillOrderDataResp();
        BeanUtils.copyProperties(one,driverKillOrderDataResp);
        return driverKillOrderDataResp;
    }

    /**
     * 点击到达代驾点后修改状态
     */
    @Override
    public void arriveLocal(String orderNo) {
        //参数判断
        if (StringUtils.isEmpty(orderNo))return;
        //查询订单数据
        Order order = super.getOne(new LambdaQueryWrapper<Order>()
               .eq(Order::getOrderNo,orderNo));
        //非空判断
        if (order == null){
            throw new GlobalException("订单不存在！");
        }
        //条件判断，只有订单已经接单才能修改，其他状态不改
        if (order.getStatus() == Constants.Order.RECEIVED_ORDER){
            order.setStatus(Constants.Order.DRIVER_ARRIVED);
            order.setArriveTime(new Date());
            super.updateById(order);
        }
    }

    /**
     * 司乘同显查询乘客的的位置以及订单的终点
     * @param orderNo
     * @return
     */
    @Override
    public LocalInfoResp localInfo(String orderNo) {
        Order order = super.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
        LocalInfoResp infoResp = new LocalInfoResp();
        BeanUtils.copyProperties(order,infoResp);
        return infoResp;
    }

    /**
     * 司机的司乘同显
     * @param orderNo
     * @return
     */
    @Override
    public LocalParams pullDriverLocal(String orderNo) {
        String format = String.format(Constants.Redis.DRIVER_LOCATION_TO_REDIS_KEY, orderNo);
        LocalParams DriverCacheLocation =(LocalParams) redisTemplate.opsForValue().get(format);
        return DriverCacheLocation;
    }
    /**
     * 乘客的司乘同显
     * @param orderNo
     * @return
     */
    @Override
    public LocalInfoResp customerLocation(String orderNo) {
        //通过orderNo去数据库查询订单信息
        Order order = super.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
        //判断订单是否存在
        if (order == null) {
            throw new GlobalException("订单不存在！");
        }
        LocalInfoResp infoResp = new LocalInfoResp();
        BeanUtils.copyProperties(order,infoResp);
        return infoResp;
    }

    /**
     * 乘客订单的回显
     * @return
     */
    @Override
    public CustomerOrderResp loadCustomerOrder() {
        Order order = super.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getCustomerId, StpUtil.getLoginIdAsLong())
                .in(Order::getStatus, Constants.Order.CUSTOMER_ORDER_IN_PROGRESS)
        );
        if (order == null){
            return null;
        }
        CustomerOrderResp customerOrderResp = new CustomerOrderResp();
        BeanUtils.copyProperties(order,customerOrderResp);
        return customerOrderResp;
    }

    /**
     * 司机点击开始代驾业务
     * @param orderNo
     */
    @Override
    @Transactional
    public void setDriveStatus(String orderNo) {

        //1.参数判断
        if (StringUtils.isEmpty(orderNo))throw new GlobalException("非法参数");
        //2.条件判断：订单状态必须是已经到达才能修改开始代驾
        Order order = super.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
        if (order == null){
            throw new GlobalException("订单不存在！");
        }
        if (order.getStatus()!= Constants.Order.DRIVER_ARRIVED){
            throw new GlobalException("订单状态不正确！");
        }
        //3.修改订单状态为开始代驾
        order.setStatus(Constants.Order.START_DRIVING);
        Date date = new Date();
        order.setStartTime(date);
        order.setUpdateTime(date);
        super.updateById(order);
        //调用方法计算等时费
        calWaitPrice(order,orderNo);
    }

    @Override
    @Transactional
    public void endDriver(String orderNo) {
        if (StringUtils.isEmpty(orderNo))throw new GlobalException("非法参数");
        //必须是订单号和状态为开始才能查到数据，修改
        Order order = super.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getStatus, Constants.Order.START_DRIVING));
        if (order == null){
            throw new GlobalException("订单状态异常！");
        }
        Date date = new Date();
        order.setEndTime(date);
        order.setStatus(Constants.Order.END_DRIVING);
        order.setUpdateTime(date);
        //调用方法
        BigDecimal realMileage = getRealMileage(orderNo);
        //设置真实里程
        int hour = DateUtil.hour(order.getCreateTime(),true);
        order.setRealMileage(realMileage);
        //返程里程
        order.setReturnMileage(realMileage);
        R<OrderBillBo> orderBillBos = ruleFeignApi.calculatePrice(new PricingParametersDto(hour, order.getExpectsMileage(), realMileage));
        if (!orderBillBos.isSuccess()){
            throw new GlobalException(orderBillBos.getMessage());
        }
        OrderBillBo orderBillBo = orderBillBos.getData();
        order.setRealOrderAmount(orderBillBo.getRealPayAmount());
        super.updateById(order);

        //账单更新
        OrderBill orderBill = orderBillMapper.selectOne(new LambdaQueryWrapper<OrderBill>()
                .eq(OrderBill::getOrderNo, order.getOrderNo()));
        orderBill.setRealOrderAmount(order.getRealOrderAmount());
        orderBill.setExceedBaseMileageAmount(orderBillBo.getExceedBaseMileageAmount());
        orderBill.setMileageAmount(orderBill.getMileageAmount());
        orderBillMapper.updateById(orderBill);
    }

    /**
     * 确认费用页面
     * @param params
     */
    @Override
    @Transactional
    public void confirm(EnterFeeParams params) {
        // 参数验证
        if (params == null) {
            throw new GlobalException("参数异常");
        }

        // 获取订单
        Order order = super.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, params.getOrderNo())
                .eq(Order::getStatus, Constants.Order.END_DRIVING));

        // 条件判断
        if (order == null) {
            throw new GlobalException("订单状态异常！");
        }

        Date date = new Date();

        // 更新订单详情
        order.setStatus(Constants.Order.DRIVER_CONFIRMATION_FEE);
        order.setUpdateTime(date);

        BigDecimal orderRealAmount = order.getRealOrderAmount();

        if (params.getOtherFree() != null && params.getOtherFree().compareTo(BigDecimal.ZERO) != 0) {
            orderRealAmount = orderRealAmount.add(params.getOtherFree());
        }

        if (params.getParkingFee() != null && params.getParkingFee().compareTo(BigDecimal.ZERO) != 0) {
            orderRealAmount = orderRealAmount.add(params.getParkingFee());
        }

        if (params.getTollFee() != null && params.getTollFee().compareTo(BigDecimal.ZERO) != 0) {
            orderRealAmount = orderRealAmount.add(params.getTollFee());
        }

        order.setRealOrderAmount(orderRealAmount);
        super.updateById(order);

        // 更新订单账单
        OrderBill orderBill = orderBillMapper.selectOne(new LambdaQueryWrapper<OrderBill>()
                .eq(OrderBill::getOrderNo, params.getOrderNo()));
        if (orderBill == null) {
            throw new GlobalException("未查询到账单");
        }
        orderBill.setTollAmount(params.getTollFee());
        orderBill.setOtherAmount(params.getOtherFree());
        orderBill.setParkingAmount(params.getParkingFee());
        orderBill.setUpdateTime(date);
        orderBill.setRealOrderAmount(orderRealAmount);
        BigDecimal realPayAmount = orderBill.getMileageAmount()
                .add(orderBill.getReturnAmont())
                .add(orderBill.getWaitingAmount())
                .add(params.getTollFee()).add(params.getParkingFee()).add(params.getOtherFree());
        orderBill.setRealPayAmount(realPayAmount);
        orderBillMapper.updateById(orderBill);

        // 获取司机信息
        R<DriverInfoBo> driverInfo = driverFeignApi.getDriverInfo(String.valueOf(StpUtil.getLoginIdAsLong()));
        if (!driverInfo.isSuccess()) {
            throw new GlobalException("获取司机信息失败");
        }
        DriverInfoBo driverInfoData = driverInfo.getData();

        // 计算分账
        ParamsDto paramsDto = new ParamsDto();
        paramsDto.setRealOrderAmount(order.getRealOrderAmount());
        paramsDto.setTodayComplaint(driverInfoData.getTodayComplaint());
        paramsDto.setTodayCancel(driverInfoData.getTodayCancel());

        R<OrderProfitBo> calSplitting = ruleFeignApi.calSplitting(paramsDto);
        if (!calSplitting.isSuccess()) {
            throw new GlobalException("计算分账失败");
        }
        OrderProfitBo calSplittingData = calSplitting.getData();

        // 保存分账数据
        OrderProfitsharing orderProfitsharing = new OrderProfitsharing();
        orderProfitsharing.setPlatformIncome(calSplittingData.getPlatformIncome());
        orderProfitsharing.setDriverIncome(calSplittingData.getDriverIncome());
        orderProfitsharing.setPlatformRate(calSplittingData.getPlatformRate());
        orderProfitsharing.setDriverRate(calSplittingData.getDriverRate());
        orderProfitsharing.setOrderNo(order.getOrderNo());
        orderProfitsharing.setOrderAmount(orderRealAmount);
        orderProfitsharing.setStatus(Constants.Order.SHAREING_AMOUNT);

        // 从redis获取司机openid
        String format = String.format(Constants.Redis.DRIVER_OPENID, StpUtil.getLoginIdAsLong());
        String openId = (String) redisTemplate.opsForValue().get(format);
        orderProfitsharing.setToUserOpenId(openId);

        orderProfitsharingService.save(orderProfitsharing);
    }

    /**
     * 司机端回显账单数据
     * @param orderNo
     * @return
     */

    @Override
    public OrderBillResp getOrderbill(String orderNo) {
        if (StringUtils.isEmpty(orderNo)) {throw new GlobalException("非法参数");}
        Order order = super.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getStatus,Constants.Order.DRIVER_CONFIRMATION_FEE));

        OrderBill orderBill = orderBillMapper.selectOne(new LambdaQueryWrapper<OrderBill>()
                .eq(OrderBill::getOrderNo,orderNo));

        OrderProfitsharing one = orderProfitsharingService.getOne(new LambdaQueryWrapper<OrderProfitsharing>()
                .eq(OrderProfitsharing::getOrderNo, orderNo));

        if (order == null || orderBill == null || one==null) {
            throw new GlobalException("订单状态异常");
        }
        OrderBillResp orderBillResp = new OrderBillResp();
        orderBillResp.setRealMileage(order.getRealMileage());
        orderBillResp.setBaseMileagePrice(orderBill.getBaseMileageAmount());
        orderBillResp.setMileageFee(orderBill.getMileageAmount());
        orderBillResp.setWaitingMinute(orderBill.getWaitingMinute());
        orderBillResp.setBaseWaitingMinute(orderBill.getWaitingMinute());
        orderBillResp.setWaitingFee(orderBill.getWaitingAmount());
        orderBillResp.setReturnMileage(order.getReturnMileage());
        orderBillResp.setBaseReturnMileage(orderBill.getFreeBaseReturnMileage());
        orderBillResp.setReturnFee(orderBill.getReturnAmont());
        orderBillResp.setParkingFee(orderBill.getParkingAmount());
        orderBillResp.setTollFee(orderBill.getTollAmount());
        orderBillResp.setOtherFee(orderBill.getOtherAmount());
        orderBillResp.setFavourFee(orderBill.getFavourAmount());
        orderBillResp.setIncentiveFee(new BigDecimal("0.00"));
        orderBillResp.setTotal(orderBill.getRealPayAmount());
        orderBillResp.setPlatformIncome(one.getPlatformIncome());
        orderBillResp.setDriverIncome(one.getDriverIncome());
        orderBillResp.setPlatformRate(one.getPlatformRate());
        return orderBillResp;
    }

    @Override
    public void sendPayOrder(String orderNo) {
        if (StringUtils.isEmpty(orderNo)) {throw new GlobalException("非法参数");}

        Order order = super.getOne(new LambdaQueryWrapper<Order>()
               .eq(Order::getOrderNo, orderNo)
               .eq(Order::getStatus,Constants.Order.DRIVER_CONFIRMATION_FEE));

        if (order == null){
            throw new GlobalException("订单状态异常");
        }
        order.setStatus(Constants.Order.UNPAID);
        order.setUpdateTime(new Date());

        //构建支付账单数据
        PayOrderData payOrderData = new PayOrderData();
        payOrderData.setAmount(order.getRealOrderAmount());
        payOrderData.setOrderNo(order.getOrderNo());
        payOrderData.setPayUserId(order.getCustomerId());
        payOrderData.setExtParams("{}");
        payOrderData.setSubject("支付账单:"+order.getRealOrderAmount());
        Message<PayOrderData> dataMessage = MessageBuilder.withPayload(payOrderData).build();
        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(
                "rocketMQ-SendPayOrder",
                "TopicSendMessage:sendPayOrderTags",
                dataMessage, order);
        log.info("sendResult:{}",sendResult);
    }

    /**
     * 乘客端加载司机发送的订单
     * @param orderNo
     * @return
     */
    @Override
    public LoadPayOrderDto loadPayOrder(String orderNo) {
        if (StringUtils.isEmpty(orderNo)) {throw new GlobalException("Please provide a valid orderNo");}
        Order order = super.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));

        OrderBill orderBill = orderBillMapper.selectOne(new LambdaQueryWrapper<OrderBill>()
                .eq(OrderBill::getOrderNo, orderNo));
        if (order == null || orderBill == null) {
            throw new GlobalException("订单未找到");
        }
        LoadPayOrderDto loadPayOrderDto = new LoadPayOrderDto();
        loadPayOrderDto.setDriverPhoto(order.getDriverPhoto());
        loadPayOrderDto.setDriverName(order.getDriverName());
        loadPayOrderDto.setDriverPhone(order.getDriverPhone());
        loadPayOrderDto.setStartPlace(order.getStartPlace());
        loadPayOrderDto.setEndPlace(order.getEndPlace());
        loadPayOrderDto.setOrderNo(order.getOrderNo());
        loadPayOrderDto.setCreateTime(order.getCreateTime());
        loadPayOrderDto.setFavourFee(order.getFavourAmount());
        loadPayOrderDto.setCarType(order.getCarType());
        loadPayOrderDto.setCarPlate(order.getCarPlate());
        loadPayOrderDto.setRealMileage(order.getRealMileage());
        loadPayOrderDto.setBaseMileage(order.getExpectsMileage());
        loadPayOrderDto.setMileageAmount(order.getExpectsOrderAmount());
        loadPayOrderDto.setWaitingMinute(orderBill.getWaitingMinute());
        loadPayOrderDto.setFreeBaseReturnMileage(orderBill.getFreeBaseReturnMileage());
        loadPayOrderDto.setReturnAmount(orderBill.getReturnAmont());
        loadPayOrderDto.setParkingAmount(orderBill.getParkingAmount());
        loadPayOrderDto.setTollAmount(orderBill.getTollAmount());
        loadPayOrderDto.setOtherAmount(orderBill.getOtherAmount());
        loadPayOrderDto.setTotalAmount(order.getRealOrderAmount());
        loadPayOrderDto.setVoucherAmount(orderBill.getVoucherAmount());
        loadPayOrderDto.setRealPay(orderBill.getRealPayAmount());
        loadPayOrderDto.setStatus(order.getStatus());
        loadPayOrderDto.setWaitingAmount(orderBill.getWaitingAmount());
        return loadPayOrderDto;
    }


    /**
     * 返回真实里程的方法
     */
    private BigDecimal getRealMileage(String orderNo) {
        // 调用大数据服务的Feign接口查询保存在HBase的司机行车轨迹
        R<List<DriverPoint>> listR = bigFeignApi.selectPoint(orderNo);
        if (!listR.isSuccess()) {
            throw new GlobalException("查询HBase失败");
        }
        // 定义一个BigDecimal变量来统计真实里程
        BigDecimal realMileage = BigDecimal.ZERO;
        // 获取查询结果
        List<DriverPoint> listRData = listR.getData();

        // 遍历查询结果，计算实际里程
        for (int i = 0; i < listRData.size() - 1; i++) {
            // 获取当前位置和下一个位置的经纬度
            String from = listRData.get(i).getLatitude() + "," + listRData.get(i).getLongitude();
            String to = listRData.get(i + 1).getLatitude() + "," + listRData.get(i + 1).getLongitude();
            // 调用restTemplate发送get请求计算两点之间的距离
            Integer distance = bigDataTemplate.calRealMileage(from, to);

            // 将距离添加到总里程中
            realMileage = realMileage.add(BigDecimal.valueOf(distance));
        }

        // 将计算出的实际里程保存到类变量中（这里假设您想要将实际里程保存在一个类变量中，但建议避免使用全局变量）

        return realMileage;
    }



    /**
     * 计算等时费
     */
    private void calWaitPrice(Order order,String orderNo){
        //4.计算等待时间（开始代驾时间-到达代驾点时间）
        Date arriveTime = order.getArriveTime();
        Date startTime = order.getStartTime();
        //等待的时间（分钟）
        long waitTime = DateUtil.between(startTime,arriveTime, DateUnit.MINUTE);
        SetOrderParam setOrderParam = ruleFeignApi.calWaitPrice(waitTime);
        //判断setOrderParam是否正常获取
        if (setOrderParam == null){
            throw new GlobalException("获取等时费参数异常！");
        }
        OrderBill orderBill = orderBillMapper.selectOne(new LambdaQueryWrapper<OrderBill>()
                .eq(OrderBill::getOrderNo, orderNo));
        orderBill.setWaitingAmount(setOrderParam.getWaitPrice());
        orderBill.setWaitingMinute((int) waitTime);
        orderBill.setFreeBaseWaitingMinute(setOrderParam.getFreeBaseWaitingMinute());
        orderBill.setExceedEveryMinuteAmount(setOrderParam.getExceedEveryMinuteAmount());
        orderBillMapper.updateById(orderBill);
    }
    /**
     * 乘客下单
     * @param paramDto
     * @return
     */
    @Override
    @Transactional
    public String createOrder(OrderParamDto paramDto) {
        // 参数判断
        if (paramDto == null) {
            throw new GlobalException("非法参数！");
        }

        // 获取当前登录用户ID
        long loginIdAsLong = StpUtil.getLoginIdAsLong();

        // 使用分布式锁防止重复下单
        RLock lock = redissonClient.getLock("order_lock:" + loginIdAsLong);
        try {
            boolean locked = lock.tryLock(3, 10, TimeUnit.SECONDS); // 等待3秒，锁定10秒
            if (!locked) {
                throw new GlobalException("重复下单！");
            }

            // 检查用户是否有未完成的订单
            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Order::getCustomerId, loginIdAsLong);
            wrapper.in(Order::getStatus, Constants.Order.CUSTOMER_ORDER_IN_PROGRESS);
            Order haveOrder = orderMapper.selectOne(wrapper);
            if (haveOrder != null) {
                throw new GlobalException("请不要重复下单！");
            }

            // 创建订单
            Order order = buildOrder(paramDto);

            // 保存订单到数据库
            boolean saveOrder = super.save(order);
            if (!saveOrder) {
                throw new GlobalException("保存订单失败！");
            }

            // 创建订单账单
            OrderBill orderBill = buildOrderBill(paramDto, order);
            boolean saveOrderBill = orderBillMapper.insert(orderBill) > 0;
            if (!saveOrderBill) {
                throw new GlobalException("保存订单账单失败！");
            }

            // 搜索司机并发送MQ消息
            searchDriversAndSendMQ(paramDto, order);

            // 返回订单号
            return order.getOrderNo();

        } catch (Exception e) {
            log.error("创建订单失败：{}", e.getMessage());
            throw new GlobalException(e.getMessage());
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    /**
     * 构建订单对象
     */
    private Order buildOrder(OrderParamDto paramDto) {
        Order order = new Order();
        BeanUtils.copyProperties(paramDto, order);
        String idStr = IdUtil.getSnowflake().nextIdStr();
        order.setOrderNo(idStr);
        order.setCustomerId(StpUtil.getLoginIdAsLong());

        int hour = DateUtil.hour(new Date(), true);
        R<OrderBillBo> orderBillBos = ruleFeignApi.calculatePrice(new PricingParametersDto(hour,
                paramDto.getExpectsMileage(),
                getRealMileage(order.getOrderNo())));
        if (!orderBillBos.isSuccess()){
            throw new GlobalException(orderBillBos.getMessage());
        }
        OrderBillBo orderBillBo = orderBillBos.getData();

        order.setExpectsOrderAmount(orderBillBo.getMileageAmount());
        order.setStatus(Constants.Order.WAIT_ORDER);
        order.setRemark("订单来了！");
        order.setReturnMileage(paramDto.getExpectsMileage());

        LoginSuccess loginSuccess = getLoginInfo();
        order.setCustomerName(loginSuccess.getName());
        order.setCustomerPhone(loginSuccess.getPhone());
        order.setCustomerPhoto(loginSuccess.getAvatar());
        order.setCreateTime(new Date());

        // 将订单信息保存到redis，用于司机抢单
        String key = String.format(Constants.Redis.ORDER_INFO_KEY, order.getOrderNo());
        redisTemplate.opsForValue().set(key, order);

        return order;
    }

    /**
     * 构建订单账单对象
     */
    private OrderBill buildOrderBill(OrderParamDto paramDto, Order order) {
        int hour = DateUtil.hour(new Date(), true);
        R<OrderBillBo> orderBillBos = ruleFeignApi.calculatePrice(new PricingParametersDto(hour,
                paramDto.getExpectsMileage(),
                getRealMileage(order.getOrderNo())));
        if (!orderBillBos.isSuccess()){
            throw new GlobalException(orderBillBos.getMessage());
        }
        OrderBillBo orderBillBo = orderBillBos.getData();

        OrderBill orderBill = new OrderBill();
        BeanUtils.copyProperties(orderBillBo, orderBill);
        long id = IdUtil.getSnowflake(1, 1).nextId();
        orderBill.setId(id);
        orderBill.setOrderDetail("账单来了");
        orderBill.setOrderNo(order.getOrderNo());
        orderBill.setCreateTime(new Date());

        return orderBill;
    }

    /**
     * 搜索司机并发送MQ消息
     */
    private void searchDriversAndSendMQ(OrderParamDto paramDto, Order order) {
        List<GeoSearchResult> geoSearchResults = searchGeo(Constants.Redis.DRIVER_LOCATION_KEY,
                Double.parseDouble(paramDto.getStartPlaceLongitude()),
                Double.parseDouble(paramDto.getStartPlaceLatitude()),
                50,
                paramDto.getExpectsMileage());

        if (geoSearchResults == null || geoSearchResults.size() == 0) {
            throw new GlobalException("未找到司机！");
        }

        for (GeoSearchResult searchResult : geoSearchResults) {
            OrderRespVo orderRespVo = OrderRespVo.builder()
                    .orderNo(order.getOrderNo())
                    .distance(searchResult.getDistance())
                    .favourFee(new BigDecimal("0.00"))
                    .expectsFee(order.getExpectsOrderAmount())
                    .from(order.getStartPlace())
                    .to(order.getEndPlace())
                    .mileage(order.getExpectsMileage()).build();

            String tag = String.format(Constants.RocketMQ.DRIVER_PULL_ORDER_TOPIC + ":" + Constants.RocketMQ.DRIVER_PULL_ORDER_TAGS, searchResult.getName());

            Message<OrderRespVo> orderRespVoMessage = MessageBuilder.withPayload(orderRespVo).build();
            SendResult sendResult = rocketMQTemplate.syncSend(tag, orderRespVoMessage);
            log.info("sendResult ：{}", sendResult);
        }
    }

    /**
     * 获取登录用户信息
     */
    private LoginSuccess getLoginInfo() {
        long loginIdAsLong = StpUtil.getLoginIdAsLong();
        String loginInfos = String.format(Constants.Redis.USER_LOGIN_INFO, loginIdAsLong);
        return (LoginSuccess) redisTemplate.opsForValue().get(loginInfos);
    }



    /**
     * 从mq拉订单
     * @return
     */
    @Override
    public List<OrderRespVo> pullOrder() {
        // OFFECT_TABLE 应该是一个持久化的数据结构，而不是在每次方法调用时都重新创建
        // 这里假设您有一个方法来获取或初始化 OFFECT_TABLE
        Long loginId = Long.valueOf(StpUtil.getLoginId().toString());

        try {
            Set<MessageQueue> messageQueues = pullConsumer.fetchSubscribeMessageQueues(Constants.RocketMQ.DRIVER_PULL_ORDER_TOPIC);

            List<OrderRespVo> result = new ArrayList<>();

            for (MessageQueue messageQueue : messageQueues) {
                // 首先从 OFFECT_TABLE 中获取偏移量，如果没有则从 Broker 中获取
                Long offset = OFFECT_TABLE.getOrDefault(messageQueue, 0L);
                if (offset == 0L) {
                    try {
                        // 从 Broker 中获取初始偏移量，可能需要确保 Broker 端支持且已配置
                        offset = pullConsumer.fetchConsumeOffset(messageQueue, false);
                        if (offset < 0) {
                            // 如果获取失败或偏移量无效，使用默认值（比如从头开始消费）
                            offset = 0L;
                        }
                    } catch (MQClientException e) {
                        // 处理获取偏移量时的异常
                        // 通常情况下，如果无法从 Broker 获取偏移量，会使用 0 或者其他默认值
                        log.error("Failed to fetch consume offset from broker for message queue: " + messageQueue, e);
                    }
                }

                String tags = String.format(Constants.RocketMQ.DRIVER_PULL_ORDER_TAGS, loginId);
                PullResult pullResult = pullConsumer.pull(messageQueue, tags, offset, 32);

                if (pullResult != null && pullResult.getPullStatus().equals(PullStatus.FOUND)) {
                    List<MessageExt> messageExtList = pullResult.getMsgFoundList();

                    if (messageExtList != null && !messageExtList.isEmpty()) {
                        for (MessageExt messageExt : messageExtList) {
                            String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
                            OrderRespVo orderMesssage = JSON.parseObject(message, OrderRespVo.class);
                            result.add(orderMesssage);
                        }
                    }
                }

                // 更新 OFFECT_TABLE 中的偏移量
                if (pullResult != null && pullResult.getNextBeginOffset() > offset) {
                    // 确保只有当新的偏移量大于旧的偏移量时才更新
                    pullConsumer.updateConsumeOffset(messageQueue, pullResult.getNextBeginOffset());
                    OFFECT_TABLE.put(messageQueue, pullResult.getNextBeginOffset());
                }
            }
            return result;
        } catch (Exception e) {
            throw new GlobalException("订单拉取失败");
        }
    }

    /**
     * 自动取消订单
     */
    @Override
    @Transactional
    public void autoCancelOrder() {
        Order order = getOrder();
        if (order == null) {
            throw new GlobalException("订单已经自动取消！");
        }
        String format = String.format(Constants.Redis.ORDER_INFO_KEY, order.getOrderNo());
        redisTemplate.delete(format);
        order.setStatus(Constants.Order.CUSTOMER_CANCEL);
        order.setUpdateTime(new Date());
        super.updateById(order);
    }

    /**
     * 乘客手动取消订单
     */
    @Override
    @Transactional
    public void handCancelOrder() {
        long loginId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<Order> eq = new LambdaQueryWrapper<Order>()
                .eq(Order::getCustomerId, loginId)
                .in(Order::getStatus, Constants.Order.ORDER_CANCELABLE);
        Order order = super.getOne(eq);
        if (order == null) {
            throw new GlobalException("未查询到可取消的订单");
        }
        //TODO:
            /**
             * 1.在rule服务计算违约金
             * 2.通过feign调用customer保存违约金
             */
        String format = String.format(Constants.Redis.ORDER_INFO_KEY, order.getOrderNo());
        redisTemplate.delete(format);
        order.setStatus(Constants.Order.CUSTOMER_CANCEL);
        order.setUpdateTime(new Date());
        super.updateById(order);
    }

    /**
     * 司机抢单
     * @param orderNo
     */

    @Override
    @Transactional
    public void killOrder(String orderNo) {
        /**
         * 1.从redis获取数据
         * 2.添加分布式锁
         * 3.参数判断
         * 4.条件判断
         */
        if (StringUtils.isEmpty(orderNo)) {
            throw new GlobalException("订单号不能为空");
        }
        /**
         * 通过redis去查询创建订单时缓存的订单
         */
        String key = String.format(Constants.Redis.ORDER_INFO_KEY, orderNo);

        //分布式锁，锁名要不一致，否则一把锁，锁多个订单容易阻塞
        RLock lock = redissonClient.getLock("order_lock：" + orderNo);
        try {
            //锁的状态
            boolean b = lock.tryLock();
            if (!b){
                throw new GlobalException("抢单失败！");
            }
            //redis查询order数据
            Order order =(Order) redisTemplate.opsForValue().get(key);
            if (order == null) {
                throw new GlobalException("未查询到订单！");
            }
            if (Constants.Order.DRIVER_ORDER_IN_PROGRESS.contains(order.getStatus())){
                throw new GlobalException("没抢到订单！");
            }
            //拼接redis的key获取司鸡的信息
            String loginInfos = String.format(Constants.Redis.USER_LOGIN_INFO, StpUtil.getLoginIdAsLong());
            LoginSuccess driverInfo = (LoginSuccess)redisTemplate.opsForValue().get(loginInfos);
            //new Order
            Order updateOrder = super.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo).eq(Order::getStatus, Constants.Order.WAIT_ORDER));
            if (updateOrder == null){
                throw new GlobalException("未查询到订单！");
            }
            Date date = new Date();
            updateOrder.setDriverId(StpUtil.getLoginIdAsLong());
            updateOrder.setAcceptTime(date);
            updateOrder.setStatus(Constants.Order.RECEIVED_ORDER);
            updateOrder.setDriverName(driverInfo.getName());
            updateOrder.setDriverPhone(driverInfo.getPhone());
            updateOrder.setDriverPhoto(driverInfo.getAvatar());
            super.updateById(updateOrder);
            Boolean delete = redisTemplate.delete(key);
            if (!delete){
                throw new GlobalException("Redis订单删除失败！");
            }
            String updateKey = String.format(Constants.Redis.ORDER_ACCEPT_KEY, updateOrder.getOrderNo());
            redisTemplate.opsForValue().set(updateKey, updateOrder);
        }finally {
            if (lock.isLocked()){
                lock.unlock();
            }
        }
    }

    /**
     * 乘客下单成功去redis查询订单是否被接单
     * @param orderNo
     * @return
     */
    @Override
    public Boolean selectOrderStatus(String orderNo) {
        String updateKey = String.format(Constants.Redis.ORDER_ACCEPT_KEY, orderNo);
        Order order = (Order)redisTemplate.opsForValue().get(updateKey);
        if (order != null && order.getStatus() == Constants.Order.RECEIVED_ORDER){
            redisTemplate.delete(updateKey);
            return true;
        }
        return false;
    }

    //查询订单根据下单的乘客id
    private Order getOrder(){
        long loginId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<Order> eq = new LambdaQueryWrapper<Order>()
                .eq(Order::getCustomerId, loginId)
                .eq(Order::getStatus, Constants.Order.WAIT_ORDER);
        Order order = super.getOne(eq);
        return order;
    }


    /**
     * 坐标搜索
     *
     * @param key       ： 司机坐标的key
     * @param longitude ：中心点的经度
     * @param latitude  ：中心点的维度
     * @param radius    ：搜索的半径
     * @return ：命中的结果
     */
    private List<GeoSearchResult> searchGeo(String key, double longitude, double latitude, double radius, BigDecimal expectMileage) {
        //指定中心坐标，千米为单位
        Circle circle = new Circle(new Point(longitude, latitude), new Distance(radius, Metrics.KILOMETERS));
        //GEO搜索的参数
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
        //结果中包含距离
        args.includeDistance()
                //结果中包含坐标
                .includeCoordinates()
                //按照中心点距离近到远排序
                .sortAscending()
                //匹配前几个司机
                .limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults = redisTemplate.opsForGeo().radius(key, circle, args);
        //拿到GEO结果列表
        List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> geoResultList = geoResults.getContent();
        //装符合结果的司机
        List<GeoSearchResult> geoSearchResult = new ArrayList<>();
        //根据距离筛选
        for (GeoResult<RedisGeoCommands.GeoLocation<Object>> geoLocationGeoResult : geoResultList) {
            String loginId = geoLocationGeoResult.getContent().getName().toString();
            String format = String.format(Constants.Redis.DRIVER_SETTING_KEY, loginId);
            DriverSettingDto driverSetting = (DriverSettingDto) redisTemplate.opsForValue().get(format);
            //如果订单的预估里程大于司机设置的订单距离，那就跳过
            if (expectMileage.intValue() > driverSetting.getOrderDistance()) {
                continue;
            }
            //orderdistance指的是司机到乘客的距离
            //Rangedistance指的是司机只接预估里程在范围公里之内的单，若是超过了则不接
            //如果下单地点到司机的距离大于了司机的Rangedistance，跳过
            if (geoLocationGeoResult.getDistance().getValue() > driverSetting.getRangeDistance()) {
                continue;
            }
            //维度
            double latitudes = geoLocationGeoResult.getContent().getPoint().getX();
            //经度
            double longitudes = geoLocationGeoResult.getContent().getPoint().getY();
            //距离
            double distance = geoLocationGeoResult.getDistance().getValue();
            //装起来
            geoSearchResult.add(new GeoSearchResult(loginId, latitudes, longitudes, distance));
            log.info("geoSearchResult：{}", geoSearchResult);
        }
        return geoSearchResult;
    }
}