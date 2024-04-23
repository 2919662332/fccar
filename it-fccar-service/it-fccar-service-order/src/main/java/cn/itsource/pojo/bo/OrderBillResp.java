package cn.itsource.pojo.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderBillResp {
   //真实里程
   private BigDecimal realMileage;
   //基础里程价格
   private BigDecimal baseMileagePrice;
   //实际里程价格
   private BigDecimal mileageFee;
   private Integer waitingMinute;
   private Integer baseWaitingMinute;
   private BigDecimal waitingFee;

   private BigDecimal returnMileage;
   private BigDecimal baseReturnMileage;
   private BigDecimal returnFee;
   private BigDecimal parkingFee;
   private BigDecimal tollFee;
   private BigDecimal otherFee;
   private BigDecimal favourFee;
   private BigDecimal incentiveFee;
   private BigDecimal platformRate;
   //汇总账单
   private BigDecimal total;
   private BigDecimal platformIncome;
   private BigDecimal driverIncome;
}
