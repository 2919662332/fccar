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
 * 
 * </p>
 *
 * @author ????
 * @since 2024-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_aggrement_self")
@Schema(name = "AggrementSelf对象", description = "")
public class AggrementSelf implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "")
    @TableField("id")
    private Long id;

    @Schema(name = "name", description = "")
    @TableField("name")
    private String name;

    @Schema(name = "phone", description = "")
    @TableField("phone")
    private String phone;

    @Schema(name = "status", description = "0:未审核，1：已经通过，2.驳回")
    @TableField("status")
    private String status;

    @Schema(name = "idCardNum", description = "")
    @TableField("id_card_num")
    private String idCardNum;

    @Schema(name = "aggeUrl", description = "")
    @TableField("agge_url")
    private String aggeUrl;

    @Schema(name = "createTime", description = "司机上传时间")
    @TableField("create_time")
    private Date createTime;

    @Schema(name = "updateTime", description = "审核时间")
    @TableField("update_time")
    private Date updateTime;

    @Schema(name = "agreementSn", description = "合同编号")
    @TableField("agreement_sn")
    private String agreementSn;

    @Schema(name = "deleted", description = "")
    @TableField("deleted")
    @TableLogic
    private String deleted;

    @Schema(name = "version", description = "")
    @TableField("version")
    @Version
    private String version;

    @Schema(name = "driverId", description = "")
    @TableField("driver_id")
    private Long driverId;

}
