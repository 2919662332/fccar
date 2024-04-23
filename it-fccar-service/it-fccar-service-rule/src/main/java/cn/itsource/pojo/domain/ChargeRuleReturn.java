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
 * 返程费计价规则
 * </p>
 *
 * @author ????
 * @since 2024-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_charge_rule_return")
@Schema(name = "ChargeRuleReturn对象", description = "返程费计价规则")
public class ChargeRuleReturn implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "name", description = "返程费名字")
    @TableField("name")
    private String name;

    @Schema(name = "freeBaseReturnMileage", description = "免费的基础里程")
    @TableField("free_base_return_mileage")
    private BigDecimal freeBaseReturnMileage;

    @Schema(name = "exceedEveryKmAmount", description = "超出基础里程，每公里金额")
    @TableField("exceed_every_km_amount")
    private BigDecimal exceedEveryKmAmount;

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

    @Schema(name = "enable", description = "是否开启: 0-开启 1-关闭")
    @TableField("enable")
    private Integer enable;

}
