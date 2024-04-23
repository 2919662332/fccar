package cn.itsource.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LocalInfoResp {
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
