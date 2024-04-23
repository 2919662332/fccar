package cn.itsource.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxPayParams {
    private String timestamp;
    private String nonceStr;
    private String packageVal;
    private String signType;
    private String paySign;
}
