package cn.itsource.remote.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ParamsDto {

    //订单金额
    private BigDecimal realOrderAmount;
    //投诉数
    private Integer todayComplaint;
    //取消订单数
    private Integer todayCancel;

}
