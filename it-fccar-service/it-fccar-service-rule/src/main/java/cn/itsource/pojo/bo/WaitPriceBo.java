package cn.itsource.pojo.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 返回的等待价格类，可能还有其他数据
 */

@Data
public class WaitPriceBo {
    private BigDecimal price;
}
