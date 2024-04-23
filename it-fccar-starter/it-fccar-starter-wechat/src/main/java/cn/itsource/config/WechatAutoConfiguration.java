package cn.itsource.config;

import cn.itsource.template.WechatLoginTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({ParamsConfig.class})
public class WechatAutoConfiguration {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public WechatLoginTemplate wechatLoginTemplate(ParamsConfig properties, RestTemplate restTemplate){
        return new WechatLoginTemplate(properties,restTemplate);
    }
}
