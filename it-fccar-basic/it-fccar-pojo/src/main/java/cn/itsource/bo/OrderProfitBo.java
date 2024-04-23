package cn.itsource.bo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProfitBo {
    //平台分账收入
    private BigDecimal platformIncome;

    //司机分账收入
    private BigDecimal driverIncome;

    //平台分账比例
    private BigDecimal platformRate;

    //司机分账比例
    private BigDecimal driverRate;
}
