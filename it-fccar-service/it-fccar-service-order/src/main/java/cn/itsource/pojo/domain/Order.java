package cn.itsource.pojo.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * <p>
 * 订单表
 * </p>
 *
 * @author ????
 * @since 2024-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_order")
@Schema(name = "Order对象", description = "订单表")
public class Order implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "主键")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "orderNo", description = "订单编号")
    @TableField("order_no")
    private String orderNo;

    @Schema(name = "customerId", description = "客户ID")
    @TableField("customer_id")
    private Long customerId;

    @Schema(name = "startPlace", description = "起始地点")
    @TableField("start_place")
    private String startPlace;

    @Schema(name = "endPlace", description = "结束地点")
    @TableField("end_place")
    private String endPlace;

    @Schema(name = "expectsMileage", description = "预估里程")
    @TableField("expects_mileage")
    private BigDecimal expectsMileage;

    @Schema(name = "realMileage", description = "实际里程")
    @TableField("real_mileage")
    private BigDecimal realMileage;

    @Schema(name = "returnMileage", description = "返程里程")
    @TableField("return_mileage")
    private BigDecimal returnMileage;

    @Schema(name = "expectsOrderAmount", description = "预估订单价格")
    @TableField("expects_order_amount")
    private BigDecimal expectsOrderAmount;

    @Schema(name = "favourAmount", description = "顾客好处费")
    @TableField("favour_amount")
    private BigDecimal favourAmount;

    @Schema(name = "incentiveAmount", description = "系统奖励费")
    @TableField("incentive_amount")
    private BigDecimal incentiveAmount;

    @Schema(name = "realOrderAmount", description = "实际订单价格")
    @TableField("real_order_amount")
    private BigDecimal realOrderAmount;

    @Schema(name = "driverId", description = "司机ID")
    @TableField("driver_id")
    private Long driverId;

    @Schema(name = "acceptTime", description = "司机接单时间")
    @TableField("accept_time")
    private Date acceptTime;

    @Schema(name = "arriveTime", description = "司机到达时间")
    @TableField("arrive_time")
    private Date arriveTime;

    @Schema(name = "startTime", description = "代驾开始时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(name = "endTime", description = "代驾结束时间")
    @TableField("end_time")
    private Date endTime;

    @Schema(name = "carPlate", description = "车牌号")
    @TableField("car_plate")
    private String carPlate;

    @Schema(name = "carType", description = "车型")
    @TableField("car_type")
    private String carType;

    @Schema(name = "status", description = "订单状态: 0-等待接单，1-已接单，2-司机已到达，3-开始代驾，4-结束代驾，5-司机确认费用，6-未付款，7-已付款，8-订单已结束，9-顾客撤单，10-司机撤单，11-事故关闭，12-其他")
    @TableField("status")
    private Integer status;

    @Schema(name = "remark", description = "订单备注信息")
    @TableField("remark")
    private String remark;

    @Schema(name = "expectMinutes", description = "预计分钟数")
    @TableField("expect_minutes")
    private Integer expectMinutes;

    @Schema(name = "startPlaceLongitude", description = "开始位置经度")
    @TableField("start_place_longitude")
    private String startPlaceLongitude;

    @Schema(name = "startPlaceLatitude", description = "开始位置纬度")
    @TableField("start_place_latitude")
    private String startPlaceLatitude;

    @Schema(name = "endPlaceLongitude", description = "结束位置经度")
    @TableField("end_place_longitude")
    private String endPlaceLongitude;

    @Schema(name = "endPlaceLatiude", description = "结束位置纬度")
    @TableField("end_place_latiude")
    private String endPlaceLatiude;

    @Schema(name = "incentiveFeeStatus", description = "系统奖励：0-未奖励 ， 1-已奖励")
    @TableField("incentive_fee_status")
    private Boolean incentiveFeeStatus;

    @Schema(name = "driverName", description = "司机名字")
    @TableField("driver_name")
    private String driverName;

    @Schema(name = "driverPhone", description = "司机电话")
    @TableField("driver_phone")
    private String driverPhone;

    @Schema(name = "driverPhoto", description = "司机头像")
    @TableField("driver_photo")
    private String driverPhoto;

    @Schema(name = "customerName", description = "乘客名字")
    @TableField("customer_name")
    private String customerName;

    @Schema(name = "customerPhone", description = "乘客电话")
    @TableField("customer_phone")
    private String customerPhone;

    @Schema(name = "customerPhoto", description = "乘客头像")
    @TableField("customer_photo")
    private String customerPhoto;

    @Schema(name = "createTime", description = "订单创建时间")
    @TableField("create_time")
    private Date createTime;

    @Schema(name = "updateTime", description = "修改时间")
    @TableField("update_time")
    private Date updateTime;

    @Schema(name = "deleted", description = "逻辑删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @Schema(name = "version", description = "乐观锁")
    @TableField("version")
    @Version
    private Integer version;

}
