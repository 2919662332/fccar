package cn.itsource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("fccar.map")
public class TencentMapParams {
    private String mapKey;
    private String url;
}
