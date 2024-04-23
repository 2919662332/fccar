package cn.itsource.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRespVo {
    private String orderNo;
    private String from;
    private String to;
    //乘客距离司机的距离
    private Double distance;
    //订单总距离
    private BigDecimal mileage;
    //小费
    private BigDecimal favourFee;
    //单费
    private BigDecimal expectsFee;
}
