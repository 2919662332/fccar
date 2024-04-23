package cn.itsource.config;

import cn.itsource.pojo.domain.Order;
import cn.itsource.service.IOrderService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RocketMQTransactionListener(txProducerGroup = "rocketMQ-SendPayOrder")
public class PayOrderTXListener  implements RocketMQLocalTransactionListener {

    @Resource
    private IOrderService orderService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        Order order = (Order) arg;
        try{
            orderService.updateById(order);
            return RocketMQLocalTransactionState.COMMIT;
        }catch (Exception e){
            log.error(e.getMessage());
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        byte[] data = (byte[]) message.getPayload();
        String json = new String(data, StandardCharsets.UTF_8);
        Order order = JSON.parseObject(json, Order.class);
        Order orderFromDB = orderService.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, order.getOrderNo()));
        return orderFromDB == null?RocketMQLocalTransactionState.ROLLBACK:RocketMQLocalTransactionState.COMMIT;
    }
}