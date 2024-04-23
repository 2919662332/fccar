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
 * 其他费用：税，渠道费
 * </p>
 *
 * @author ????
 * @since 2024-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_profitsharing_rule_complaints")
@Schema(name = "ProfitsharingRuleComplaints对象", description = "其他费用：税，渠道费")
public class ProfitsharingRuleComplaints implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "start", description = "比例")
    @TableField("start")
    private Integer start;

    @Schema(name = "end", description = "")
    @TableField("end")
    private Integer end;

    @Schema(name = "proportion", description = "")
    @TableField("proportion")
    private BigDecimal proportion;

}
