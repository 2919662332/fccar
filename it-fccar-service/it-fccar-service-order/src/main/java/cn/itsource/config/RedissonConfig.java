package cn.itsource.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class RedissonConfig {

    @Resource
    private RedisProperties redisProperties;

    //创建客户端
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+redisProperties.getHost()+":"+redisProperties.getPort()).setPassword(redisProperties.getPassword());
        return Redisson.create(config);
    }
}
