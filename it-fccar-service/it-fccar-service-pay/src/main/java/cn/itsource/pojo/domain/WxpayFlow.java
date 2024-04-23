package cn.itsource.pojo.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
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
 * 
 * </p>
 *
 * @author ????
 * @since 2024-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_wxpay_flow")
@Schema(name = "WxpayFlow对象", description = "")
public class WxpayFlow implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "amount", description = "金额")
    @TableField("amount")
    private BigDecimal amount;

    @Schema(name = "spAppid", description = "APPID")
    @TableField("sp_appid")
    private String spAppid;

    @Schema(name = "spMchid", description = "商户号ID")
    @TableField("sp_mchid")
    private String spMchid;

    @Schema(name = "bankType", description = "银行类型")
    @TableField("bank_type")
    private String bankType;

    @Schema(name = "outTradeNo", description = "订单号")
    @TableField("out_trade_no")
    private String outTradeNo;

    @Schema(name = "payerOpenid", description = "支付者")
    @TableField("payer_openid")
    private String payerOpenid;

    @Schema(name = "userId", description = "支付用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(name = "successTime", description = "最后处理时间")
    @TableField("success_time")
    private Date successTime;

    @Schema(name = "tradeState", description = "支付状态")
    @TableField("trade_state")
    private String tradeState;

    @Schema(name = "tradeStateDesc", description = "备注")
    @TableField("trade_state_desc")
    private String tradeStateDesc;

    @Schema(name = "tradeType", description = "交易类型")
    @TableField("trade_type")
    private String tradeType;

    @Schema(name = "attach", description = "附加数据")
    @TableField("attach")
    private String attach;

}
