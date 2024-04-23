package cn.itsource.autoconfig;

import cn.itsource.config.SaTokenConfigure;
import cn.itsource.config.StpInterfaceImpl;
import cn.itsource.config.redisConfig;
import cn.itsource.intercepter.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Import({redisConfig.class, SaTokenConfigure.class})
public class SatokenAutoConfiguration {

    @Bean
    public StpInterfaceImpl StpInterfaceImpl(RedisTemplate<String,Object> redisTemplate){
        return new StpInterfaceImpl(redisTemplate);
    }
    @Bean
    public TokenInterceptor tokenInterceptor(){
        return new TokenInterceptor();
    }

}
