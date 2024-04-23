package cn.itsource.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DriverKillOrderDataResp {
    private String orderNo;
    private String customerPhoto;
    private String customerName;
    private String customerPhone;
    private String customerId;
    private String startPlace;
    private String startLongitude;
    private String startLatitude;
    private String endPlace;
    private String endLongitude;
    private String endLatitude;
    private String favourFee;
    private String carPlate;
    private String carType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+8")
    private Date createTime;
    private Integer status;
}
