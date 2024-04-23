package cn.itsource.listener;

import cn.itsource.pojo.bo.GetuiSourceBo;
import cn.itsource.template.GetuiTemplate;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class GetuiMessagePublisher implements ApplicationListener<MySelfMessageEvent> {

    @Resource
    private GetuiTemplate getuiTemplate;
    @Override
    public void onApplicationEvent(MySelfMessageEvent event) {
        /*
         * SendPayOrderConsumer发的消息在这里拿
         */
        GetuiSourceBo eventSource =(GetuiSourceBo) event.getSource();
        log.info("GetuiSource：{}",eventSource);
        getuiTemplate.goBindCid(eventSource.getAlias(), JSON.toJSONString(eventSource));
    }
}

