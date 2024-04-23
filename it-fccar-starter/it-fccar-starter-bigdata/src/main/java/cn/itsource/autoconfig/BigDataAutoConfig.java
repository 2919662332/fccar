package cn.itsource.autoconfig;

import cn.itsource.config.TencentMapParams;
import cn.itsource.template.BigDataTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(TencentMapParams.class)
public class BigDataAutoConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public BigDataTemplate bigDataTemplate(TencentMapParams tencentMapParams, RestTemplate restTemplate) {
        return new BigDataTemplate(tencentMapParams,restTemplate);
    }
}
