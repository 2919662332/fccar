package cn.itsource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties("fccar.getui")
public class ConfigParam {
    private String setMaxConncetSetting;
    private String setMaxConnectNum;
    private String appid;
    private String appkey;
    private String MasterSecret;
    private String domain;
}
