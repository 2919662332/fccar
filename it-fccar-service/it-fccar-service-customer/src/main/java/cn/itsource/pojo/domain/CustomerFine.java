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
 * 乘客罚款
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer_fine")
@Schema(name = "CustomerFine对象", description = "乘客罚款")
public class CustomerFine implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "customerId", description = "客户ID")
    @TableField("customer_id")
    private Long customerId;

    @Schema(name = "orderId", description = "订单ID")
    @TableField("order_id")
    private Long orderId;

    @Schema(name = "amount", description = "金额")
    @TableField("amount")
    private BigDecimal amount;

    @Schema(name = "remark", description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(name = "status", description = "1未缴纳，2已缴纳")
    @TableField("status")
    private Integer status;

    @Schema(name = "createTime", description = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @Schema(name = "updateTime", description = "修改时间")
    @TableField("update_time")
    private Date updateTime;

    @Schema(name = "deleted", description = "删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

}
