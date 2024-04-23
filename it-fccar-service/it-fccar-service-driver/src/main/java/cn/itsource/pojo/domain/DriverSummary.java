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
 * 司机结算对象
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_driver_summary")
@Schema(name = "DriverSummary对象", description = "司机结算对象")
public class DriverSummary implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "level", description = "等级")
    @TableField("level")
    private Integer level;

    @Schema(name = "totalOrders", description = "总接单数")
    @TableField("total_orders")
    private Integer totalOrders;

    @Schema(name = "weekOrders", description = "每周接单数")
    @TableField("week_orders")
    private Integer weekOrders;

    @Schema(name = "goodComments", description = "好评数")
    @TableField("good_comments")
    private Integer goodComments;

    @Schema(name = "appeal", description = "申述次数")
    @TableField("appeal")
    private Integer appeal;

    @Schema(name = "totalComplaint", description = "总投诉次数")
    @TableField("total_complaint")
    private Integer totalComplaint;

    @Schema(name = "todayTotalCancel", description = "总取消订单数")
    @TableField("today_total_cancel")
    private Integer todayTotalCancel;

    @Schema(name = "driveDuration", description = "驾驶总小时数")
    @TableField("drive_duration")
    private Integer driveDuration;

    @Schema(name = "tradeOrders", description = "总成交订单数")
    @TableField("trade_orders")
    private Integer tradeOrders;

    @Schema(name = "todayTradeOrders", description = "今日成单数")
    @TableField("today_trade_orders")
    private Integer todayTradeOrders;

    @Schema(name = "todayComplaint", description = "今日投诉次数")
    @TableField("today_complaint")
    private Integer todayComplaint;

    @Schema(name = "todayCancel", description = "今日取消订单数")
    @TableField("today_cancel")
    private Integer todayCancel;

    @Schema(name = "todayIncome", description = "今日收入")
    @TableField("today_income")
    private BigDecimal todayIncome;

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
