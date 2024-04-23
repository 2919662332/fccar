package cn.itsource.config;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${rocketmq.name-server}")
    private String nameServerAddr;

    //RocketMQ手动拉取消息
    @Bean
    public DefaultMQPullConsumer pullConsumer(){
        //定义一个pull模式的消费者
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("service-order-consumer");
        consumer.setNamesrvAddr(nameServerAddr);
        try {
            consumer.setMessageModel(MessageModel.CLUSTERING);
            consumer.start();
            return consumer;
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }
}