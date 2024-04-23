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
 * 客户
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer")
@Schema(name = "Customer对象", description = "客户")
public class Customer implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "主键")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "sex", description = "性别")
    @TableField("sex")
    private Integer sex;

    @Schema(name = "phone", description = "电话")
    @TableField("phone")
    private String phone;

    @Schema(name = "email", description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(name = "createTime", description = "注册时间")
    @TableField("create_time")
    private Date createTime;

    @Schema(name = "updateTime", description = "最后修改时间")
    @TableField("update_time")
    private Date updateTime;

    @Schema(name = "version", description = "版本")
    @TableField("version")
    @Version
    private Integer version;

    @Schema(name = "deleted", description = "删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @Schema(name = "openId", description = "微信ID")
    @TableField("open_id")
    private String openId;

    @Schema(name = "bitState", description = "位状态")
    @TableField("bit_state")
    private Long bitState;

    @Schema(name = "name", description = "名字")
    @TableField("name")
    private String name;

    @Schema(name = "level", description = "等级0青铜,1白银，2黄金,3白金,4大师,5宗师,6王者")
    @TableField("level")
    private String level;

}
