package cn.itsource.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *
 * 用于创建订单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderParamDto {
    //开始位置和结束位置
   private String startPlace;
   private String endPlace;
   private String startPlaceLongitude;
   private String startPlaceLatitude;
   private String endPlaceLongitude;
   private String endPlaceLatiude;
   //预估里程
   private BigDecimal expectsMileage;
   //预计分钟数
   private Integer expectMinutes;

   //车型车号
   private String carPlate;
   private String carType;
}
