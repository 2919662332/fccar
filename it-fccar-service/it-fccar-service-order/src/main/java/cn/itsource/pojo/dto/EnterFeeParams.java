package cn.itsource.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EnterFeeParams {
   private String orderNo;
   //路桥费
   private BigDecimal tollFee;
   //停车费
   private BigDecimal parkingFee;
   //其他费用
   private BigDecimal otherFree;
}
