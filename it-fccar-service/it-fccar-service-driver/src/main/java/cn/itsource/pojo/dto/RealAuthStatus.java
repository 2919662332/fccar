package cn.itsource.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealAuthStatus {
    private Boolean realAuthSuccess;
    private Integer driveDuration;
    private BigDecimal todayIncome;
    private Integer todayTradeOrders;
    private Long bitStatus;
}
