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
 * 司机罚款
 * </p>
 *
 * @author ????
 * @since 2024-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_profitsharing_rule_deduct_cancel")
@Schema(name = "ProfitsharingRuleDeductCancel对象", description = "司机罚款")
public class ProfitsharingRuleDeductCancel implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "driverRatioDeduct", description = "抽出抽成比例")
    @TableField("driver_ratio_deduct")
    private BigDecimal driverRatioDeduct;

    @Schema(name = "numberFrom", description = "数量从多少")
    @TableField("number_from")
    private Integer numberFrom;

    @Schema(name = "numberTo", description = "数量到多少")
    @TableField("number_to")
    private Integer numberTo;

}
