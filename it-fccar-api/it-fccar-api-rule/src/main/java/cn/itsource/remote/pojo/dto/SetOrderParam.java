package cn.itsource.remote.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SetOrderParam {
    //等时费
    private BigDecimal waitPrice;
    //等时时长
    private Integer waitingMinute;
    //免费等待乘客分钟数
    private Integer freeBaseWaitingMinute;
    //等时费用
    private BigDecimal exceedEveryMinuteAmount;
}
