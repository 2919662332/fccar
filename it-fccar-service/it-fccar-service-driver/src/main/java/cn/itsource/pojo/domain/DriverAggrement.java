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
 * 司机的协议签署
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_driver_aggrement")
@Schema(name = "DriverAggrement对象", description = "司机的协议签署")
public class DriverAggrement implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "driverId", description = "时机ID")
    @TableField("driver_id")
    private Long driverId;

    @Schema(name = "agreementSn", description = "合同编号")
    @TableField("agreement_sn")
    private String agreementSn;

    @Schema(name = "sendToDriverTime", description = "平台寄出时间")
    @TableField("send_to_driver_time")
    private Date sendToDriverTime;

    @Schema(name = "driverSendBackTime", description = "司机寄回时间")
    @TableField("driver_send_back_time")
    private Date driverSendBackTime;

    @Schema(name = "agreementPathOriginal", description = "协议云储存地址:源文件")
    @TableField("agreement_path_original")
    private String agreementPathOriginal;

    @Schema(name = "agreementPathSigned", description = "协议云储存地址:已签署")
    @TableField("agreement_path_signed")
    private String agreementPathSigned;

    @Schema(name = "status", description = "签署状态：0未生成，1已生成，2已寄出，3成功签署，4签署失败")
    @TableField("status")
    private Integer status;

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
