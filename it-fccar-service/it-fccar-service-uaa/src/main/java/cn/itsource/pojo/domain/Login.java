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
 * 登录表
 * </p>
 *
 * @author zhaodi
 * @since 2024-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_login")
@Schema(name = "Login对象", description = "登录表")
public class Login implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "username", description = "用户名")
    @TableField("username")
    private String username;

    @Schema(name = "password", description = "密码")
    @TableField("password")
    private String password;

    @Schema(name = "type", description = "0是平台账户，1是司机账户，2是客户账户")
    @TableField("type")
    private Integer type;

    @Schema(name = "enabled", description = "账号启用")
    @TableField("enabled")
    private Boolean enabled;

    @Schema(name = "avatar", description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(name = "admin", description = "管理员")
    @TableField("admin")
    private Boolean admin;

    @Schema(name = "nickName", description = "昵称")
    @TableField("nick_name")
    private String nickName;

    @Schema(name = "openId", description = "微信ID")
    @TableField("open_id")
    private String openId;

    @Schema(name = "hxId", description = "环信ID")
    @TableField("hx_id")
    private String hxId;

    @Schema(name = "name", description = "姓名")
    @TableField("name")
    private String name;

    @Schema(name = "phone", description = "手机号")
    @TableField("phone")
    private String phone;

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
