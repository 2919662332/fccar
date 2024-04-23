package cn.itsource.autoconfig;

import cn.itsource.config.ConfigParam;
import cn.itsource.template.GetuiTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ConfigParam.class)
public class GetuiAutoConfig {

    @Bean
    public GetuiTemplate getuiTemplate(ConfigParam configParam ){
        return new GetuiTemplate(configParam);
    }
}
