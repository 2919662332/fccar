package cn.itsource.autoconfig;

import cn.itsource.config.WxPayConfig;
import cn.itsource.template.WxPayTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableConfigurationProperties(WxPayConfig.class)
public class WxAutoConfig {
    @Bean
    public WxPayTemplate wxPayTemplate(WxPayConfig wxPayConfig, RedisTemplate<String,Object> redisTemplate){
        return new WxPayTemplate(wxPayConfig,redisTemplate);
    }
}
