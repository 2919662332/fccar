package cn.itsource.pojo.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 * 
 * </p>
 *
 * @author ????
 * @since 2024-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_pay_order")
@Schema(name = "PayOrder对象", description = "")
public class PayOrder implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "createTime", description = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @Schema(name = "updateTime", description = "修改时间")
    @TableField("update_time")
    private Date updateTime;

    @Schema(name = "amount", description = "支付金额")
    @TableField("amount")
    private BigDecimal amount;

    @Schema(name = "payType", description = "支付方式:0余额直接，1微信，2支付宝,3银联")
    @TableField("pay_type")
    private Integer payType;

    @Schema(name = "orderNo", description = "订单号")
    @TableField("order_no")
    private String orderNo;

    @Schema(name = "payUserId", description = "用户ID")
    @TableField("pay_user_id")
    private Long payUserId;

    @Schema(name = "extParams", description = "扩展参数，JSON格式")
    @TableField("ext_params")
    private String extParams;

    @Schema(name = "subject", description = "描述")
    @TableField("subject")
    private String subject;

    @Schema(name = "payStatus", description = "0：待支付 1.支付成功 2：支付超时")
    @TableField("pay_status")
    private Integer payStatus;

    @Schema(name = "version", description = "乐观锁")
    @TableField("version")
    @Version
    private Integer version;

    @Schema(name = "payOrderNo", description = "支付单号")
    @TableField("pay_order_no")
    private String payOrderNo;

    @Schema(name = "resultDesc", description = "结果说明")
    @TableField("result_desc")
    private String resultDesc;

    @Schema(name = "deleted", description = "状态删除")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @Schema(name = "successPayTime", description = "支付成功时间")
    @TableField("success_pay_time")
    private Date successPayTime;

    @Schema(name = "applyPayTime", description = "发起支付的时间")
    @TableField("apply_pay_time")
    private Date applyPayTime;

}
