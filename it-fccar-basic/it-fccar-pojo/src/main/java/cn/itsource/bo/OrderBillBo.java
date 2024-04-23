package cn.itsource.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderBillBo {

    //实际订单价格
    private BigDecimal realOrderAmount;

    //实付款金额
    private BigDecimal realPayAmount;

    //基础里程（公里）
    private BigDecimal baseMileage;

    //基础里程价格
    private BigDecimal baseMileageAmount;

    //超出基础里程的价格
    private BigDecimal exceedBaseMileageAmount;

    //里程费
    private BigDecimal mileageAmount;

    //等时时长
    private Integer waitingMinute;

    //免费等待乘客分钟数
    private Integer freeBaseWaitingMinute;

    //等时费用
    private BigDecimal waitingAmount;

    //返程费免费公里数
    private BigDecimal freeBaseReturnMileage;

    //超出基础里程部分每公里收取的费用
    private BigDecimal exceedBaseReturnEveryKmAmount;

    //等时超出基础免费每分钟价格
    private BigDecimal exceedEveryMinuteAmount;

    //返程费
    private BigDecimal returnAmont;

    //其他费用
    private BigDecimal otherAmount;

    //停车费
    private BigDecimal parkingAmount;
}
