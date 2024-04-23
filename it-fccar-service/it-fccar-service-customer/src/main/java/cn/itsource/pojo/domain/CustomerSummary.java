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
 * 乘客数据汇总
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer_summary")
@Schema(name = "CustomerSummary对象", description = "乘客数据汇总")
public class CustomerSummary implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "ID同CoustomerId")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "totalOrders", description = "订单数")
    @TableField("total_orders")
    private Integer totalOrders;

    @Schema(name = "totalFine", description = "总罚款")
    @TableField("total_fine")
    private BigDecimal totalFine;

    @Schema(name = "voucherNumbers", description = "代金券数量")
    @TableField("voucher_numbers")
    private Integer voucherNumbers;

    @Schema(name = "cancelOrders", description = "取消订单数")
    @TableField("cancel_orders")
    private Integer cancelOrders;

}
