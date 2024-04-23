package cn.itsource.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadPayOrderDto {
    private String driverPhoto;
    private String driverName;
    private String driverPhone;
    private String startPlace;
    private String endPlace;
    private String orderNo;
    private Date createTime;
    //小费
    private BigDecimal favourFee;
    private String carType;
    private String carPlate;
    private BigDecimal realMileage;
    private BigDecimal baseMileage;
    private BigDecimal mileageAmount;
    private Integer waitingMinute;
    private BigDecimal waitingAmount;
    private BigDecimal freeBaseReturnMileage;
    private BigDecimal returnAmount;
    private BigDecimal parkingAmount;
    private BigDecimal tollAmount;
    private BigDecimal otherAmount;
    private BigDecimal totalAmount;
    private BigDecimal voucherAmount;
    private BigDecimal realPay;
    private Integer status;

}
