package cn.itsource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Data
@ConfigurationProperties("fccar.auth")
public class AuthConfig {
    private String host;
    private String path;
    private String method;
    private String appcode;
}
