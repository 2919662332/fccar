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
 * 钱包流水
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_driver_wallet_flow")
@Schema(name = "DriverWalletFlow对象", description = "钱包流水")
public class DriverWalletFlow implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "notes", description = "描述")
    @TableField("notes")
    private String notes;

    @Schema(name = "amount", description = "金额")
    @TableField("amount")
    private BigDecimal amount;

    @Schema(name = "flowType", description = "流水类型")
    @TableField("flow_type")
    private Integer flowType;

    @Schema(name = "createTime", description = "发生事件")
    @TableField("create_time")
    private Date createTime;

    @Schema(name = "income", description = "进账或出账")
    @TableField("income")
    private Boolean income;

    @Schema(name = "walletId", description = "钱包ID")
    @TableField("wallet_id")
    private Long walletId;

    @Schema(name = "walletAmount", description = "变化之后金额")
    @TableField("wallet_amount")
    private BigDecimal walletAmount;

    @Schema(name = "uniqueNo", description = "唯一标识")
    @TableField("unique_no")
    private String uniqueNo;

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
