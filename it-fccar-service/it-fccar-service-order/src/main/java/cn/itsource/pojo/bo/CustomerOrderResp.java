package cn.itsource.pojo.bo;

import lombok.Data;

@Data
public class CustomerOrderResp {
    //订单号
    private String orderNo;
    //开始得位置
    private String startPlace;
    //结束的位置
    private String endPlace;
    //开始位置经度
    private String startPlaceLongitude;

    //开始位置纬度
    private String startPlaceLatitude;

    //结束位置经度
    private String endPlaceLongitude;

    //结束位置经度
    private String endPlaceLatiude;
    //状态
    private Integer status;
}
