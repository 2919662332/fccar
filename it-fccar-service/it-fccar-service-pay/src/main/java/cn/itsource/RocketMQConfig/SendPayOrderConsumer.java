package cn.itsource.RocketMQConfig;

import cn.itsource.bo.PayOrderData;
import cn.itsource.listener.MySelfMessageEvent;
import cn.itsource.pojo.bo.GetuiSourceBo;
import cn.itsource.service.IPayOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@RocketMQMessageListener(
        consumerGroup = "rocketMQ-SendPayOrder",
        topic = "TopicSendMessage")
@Component
public class SendPayOrderConsumer implements RocketMQListener<MessageExt> {

    @Resource
    private IPayOrderService payOrderService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onMessage(MessageExt message) {
        byte[] body = message.getBody();
        String payOrderMessage = new String(body, StandardCharsets.UTF_8);

        // 使用 Jackson 将 JSON 转换为 PayOrderData 对象
        ObjectMapper objectMapper = new ObjectMapper();
        PayOrderData payOrder;

        try {
            payOrder = objectMapper.readValue(payOrderMessage, PayOrderData.class);
            payOrderService.create(payOrder);
            /**
             * 构建推送消息的对象
             */
            GetuiSourceBo getuiSourceBo = GetuiSourceBo.builder()
                    .alias(payOrder.getPayUserId().toString())
                    .status(1) //1 --> 支付账单消息
                    .content(payOrder.getOrderNo()).build();

            //触发ApplicationListener事件推送消息
            applicationEventPublisher.publishEvent(new MySelfMessageEvent(getuiSourceBo));

        } catch (JsonProcessingException e) {
            log.error("JSON 转换异常：", e);
            return;
        }

        log.info("成功消费订单：{}", payOrder);
    }
}