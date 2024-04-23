package cn.itsource.dto;

import lombok.Data;

import java.util.Date;

/**
 * 订单正常完成参数
 */
@Data
public class OrderPriceParamsDto {
    //订单创建的时间(只有小时数)
    private Integer orderDate;
    //公里数
    private Integer distance;
    //时长
    private Integer duration;
    //车牌
    private String carPlate;
    //车型
    private String carType;

}
