package cn.itsource.remote.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用于订单计费
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingParametersDto {
    //订单创建的时间（只取小时，24小时制）
    private Integer orderDate;
    //预估里程
    private BigDecimal expectsMileage;
    //真实里程
    private BigDecimal realMileage;
}
