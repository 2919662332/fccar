package cn.itsource.pojo.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * <p>
 * 平台司机分账表
 * </p>
 *
 * @author ????
 * @since 2024-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_order_profitsharing")
@Schema(name = "OrderProfitsharing对象", description = "平台司机分账表")
public class OrderProfitsharing implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "主键")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "orderNo", description = "订单号")
    @TableField("order_no")
    private String orderNo;

    @Schema(name = "orderAmount", description = "总费用")
    @TableField("order_amount")
    private BigDecimal orderAmount;

    @Schema(name = "status", description = "分账状态，0未分账，1已分账")
    @TableField("status")
    private Integer status;

    @Schema(name = "platformIncome", description = "平台分账收入")
    @TableField("platform_income")
    private BigDecimal platformIncome;

    @Schema(name = "driverIncome", description = "司机分账收入")
    @TableField("driver_income")
    private BigDecimal driverIncome;

    @Schema(name = "platformRate", description = "平台分账比例")
    @TableField("platform_rate")
    private BigDecimal platformRate;

    @Schema(name = "driverRate", description = "司机分账比例")
    @TableField("driver_rate")
    private BigDecimal driverRate;

    @Schema(name = "toUserOpenId", description = "分账人微信ID")
    @TableField("to_user_open_id")
    private String toUserOpenId;

}
