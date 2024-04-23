package cn.itsource.pojo.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单正常完成计费
 */

@Data
public class OrderPriceBo {
    private BigDecimal orderPrice;
}
