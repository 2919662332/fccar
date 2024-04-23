package cn.itsource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "fccar.app")
@Data
public class ParamsConfig {
    private String wxSecret;//wx56e1db0e88bacd6b
    private String wxAppid;
    private String url;
    private String accessUrl;
    private String phoneUrl;
}