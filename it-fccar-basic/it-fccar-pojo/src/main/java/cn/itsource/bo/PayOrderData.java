package cn.itsource.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayOrderData {

    private BigDecimal amount;

    private String orderNo;

    private Long payUserId;
    //扩展参数
    private String extParams;

    //描述
    private String subject;
}
