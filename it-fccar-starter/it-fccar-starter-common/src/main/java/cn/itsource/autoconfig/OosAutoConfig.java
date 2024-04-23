package cn.itsource.autoconfig;

import cn.itsource.pojo.config.ParamsConfig;
import cn.itsource.template.OssTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ParamsConfig.class)
public class OosAutoConfig {
    @Bean
    public OssTemplate ossTemplate(ParamsConfig paramsConfig){
        return new OssTemplate(paramsConfig);
    }
}
