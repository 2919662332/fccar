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
 * 订单账单表
 * </p>
 *
 * @author ????
 * @since 2024-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_order_bill")
@Schema(name = "OrderBill对象", description = "订单账单表")
public class OrderBill implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "主键")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "realOrderAmount", description = "订单总金额")
    @TableField("real_order_amount")
    private BigDecimal realOrderAmount;

    @Schema(name = "realPayAmount", description = "实付款金额")
    @TableField("real_pay_amount")
    private BigDecimal realPayAmount;

    @Schema(name = "tollAmount", description = "过路费")
    @TableField("toll_amount")
    private BigDecimal tollAmount;

    @Schema(name = "favourAmount", description = "顾客好处费")
    @TableField("favour_amount")
    private BigDecimal favourAmount;

    @Schema(name = "incentiveAmount", description = "系统奖励费")
    @TableField("incentive_amount")
    private BigDecimal incentiveAmount;

    @Schema(name = "voucherAmount", description = "代金券面额")
    @TableField("voucher_amount")
    private BigDecimal voucherAmount;

    @Schema(name = "orderDetail", description = "详情")
    @TableField("order_detail")
    private String orderDetail;

    @Schema(name = "baseMileage", description = "基础里程（公里）")
    @TableField("base_mileage")
    private BigDecimal baseMileage;

    @Schema(name = "baseMileageAmount", description = "基础里程价格")
    @TableField("base_mileage_amount")
    private BigDecimal baseMileageAmount;

    @Schema(name = "exceedBaseMileageAmount", description = "超出基础里程的价格")
    @TableField("exceed_base_mileage_amount")
    private BigDecimal exceedBaseMileageAmount;

    @Schema(name = "mileageAmount", description = "里程费")
    @TableField("mileage_amount")
    private BigDecimal mileageAmount;

    @Schema(name = "waitingMinute", description = "等时时长")
    @TableField("waiting_minute")
    private Integer waitingMinute;

    @Schema(name = "freeBaseWaitingMinute", description = "免费等待乘客分钟数")
    @TableField("free_base_waiting_minute")
    private Integer freeBaseWaitingMinute;

    @Schema(name = "waitingAmount", description = "等时费用")
    @TableField("waiting_amount")
    private BigDecimal waitingAmount;

    @Schema(name = "freeBaseReturnMileage", description = "返程费免费公里数")
    @TableField("free_base_return_mileage")
    private BigDecimal freeBaseReturnMileage;

    @Schema(name = "exceedBaseReturnEveryKmAmount", description = "超出基础里程部分每公里收取的费用")
    @TableField("exceed_base_return_every_km_amount")
    private BigDecimal exceedBaseReturnEveryKmAmount;

    @Schema(name = "exceedEveryMinuteAmount", description = "等时超出基础免费每分钟价格")
    @TableField("exceed_every_minute_amount")
    private BigDecimal exceedEveryMinuteAmount;

    @Schema(name = "returnAmont", description = "返程费")
    @TableField("return_amont")
    private BigDecimal returnAmont;

    @Schema(name = "orderNo", description = "订单号")
    @TableField("order_no")
    private String orderNo;

    @Schema(name = "otherAmount", description = "其他费用")
    @TableField("other_amount")
    private BigDecimal otherAmount;

    @Schema(name = "parkingAmount", description = "停车费")
    @TableField("parking_amount")
    private BigDecimal parkingAmount;

    @Schema(name = "createTime", description = "创建时间")
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
