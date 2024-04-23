package cn.itsource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties("fccar.wxpay")
public class WxPayConfig {
    private String merchantId;
    private String appid;
    private String privateKeyPath;
    private String merchantSerialNumber;
    private String apiV3Key;
    private String notifyUrl;
}
