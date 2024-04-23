package cn.itsource.pojo.vo;

import lombok.Data;

@Data
public class AccessTokenResultVo {
    private String access_token;
    private Integer expires_in;
}
