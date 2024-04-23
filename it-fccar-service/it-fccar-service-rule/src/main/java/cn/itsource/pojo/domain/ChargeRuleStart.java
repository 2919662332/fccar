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
 * 计价规则-起步价
 * </p>
 *
 * @author ????
 * @since 2024-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_charge_rule_start")
@Schema(name = "ChargeRuleStart对象", description = "计价规则-起步价")
public class ChargeRuleStart implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "name", description = "名称")
    @TableField("name")
    private String name;

    @Schema(name = "hourStart", description = "开始时间点")
    @TableField("hour_start")
    private Integer hourStart;

    @Schema(name = "hourEnd", description = "结束时间点")
    @TableField("hour_end")
    private Integer hourEnd;

    @Schema(name = "baseMileage", description = "基础里程：千米")
    @TableField("base_mileage")
    private BigDecimal baseMileage;

    @Schema(name = "exceedEveryKmAmount", description = "超出起步价每公里价格")
    @TableField("exceed_every_km_amount")
    private BigDecimal exceedEveryKmAmount;

    @Schema(name = "amount", description = "价格")
    @TableField("amount")
    private BigDecimal amount;

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
