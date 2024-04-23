package cn.itsource.domian;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class PayOrderDto {

    private BigDecimal amount;

    private String subject;

    private String payOrderNo;

}
