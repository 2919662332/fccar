package cn.itsource.autoconfig;

import cn.itsource.config.AuthConfig;
import cn.itsource.template.AuthTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AuthConfig.class)
public class AuthAutoConfig {
    @Bean
    public AuthTemplate authTemplate(AuthConfig authConfig){
        return new AuthTemplate(authConfig);
    }
}
