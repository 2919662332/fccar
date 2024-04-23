package cn.itsource.pojo.domain;

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
 * 材料审核日志
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_driver_material_auth_log")
@Schema(name = "DriverMaterialAuthLog对象", description = "材料审核日志")
public class DriverMaterialAuthLog implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "authMaterialId", description = "实名材料ID")
    @TableField("auth_material_id")
    private Long authMaterialId;

    @Schema(name = "realAuthStatus", description = "状态")
    @TableField("real_auth_status")
    private Integer realAuthStatus;

    @Schema(name = "auditTime", description = "审核时间")
    @TableField("audit_time")
    private Date auditTime;

    @Schema(name = "auditUserId", description = "审核人")
    @TableField("audit_user_id")
    private Long auditUserId;

    @Schema(name = "auditRemark", description = "审核备注")
    @TableField("audit_remark")
    private String auditRemark;

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
