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
 * 乘客的钱包
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer_wallet")
@Schema(name = "CustomerWallet对象", description = "乘客的钱包")
public class CustomerWallet implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "amount", description = "余额")
    @TableField("amount")
    private BigDecimal amount;

    @Schema(name = "password", description = "支付密码")
    @TableField("password")
    private String password;

    @Schema(name = "deleted", description = "逻辑删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @Schema(name = "version", description = "乐观锁")
    @TableField("version")
    @Version
    private Integer version;

    @Schema(name = "createTime", description = "创建日期")
    @TableField("create_time")
    private Date createTime;

    @Schema(name = "updateTime", description = "修改日期")
    @TableField("update_time")
    private Date updateTime;

}
