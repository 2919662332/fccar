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
 * 司机配置
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_driver_setting")
@Schema(name = "DriverSetting对象", description = "司机配置")
public class DriverSetting implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "autoAccept", description = "自动抢单")
    @TableField("auto_accept")
    private Boolean autoAccept;

    @Schema(name = "orientation", description = "定向抢单")
    @TableField("orientation")
    private Boolean orientation;

    @Schema(name = "listenService", description = "自动听单")
    @TableField("listen_service")
    private Boolean listenService;

    @Schema(name = "orderDistance", description = "接单条件：订单距离(公里)")
    @TableField("order_distance")
    private Integer orderDistance;

    @Schema(name = "rangeDistance", description = "接单条件：方圆n公里")
    @TableField("range_distance")
    private Integer rangeDistance;

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
