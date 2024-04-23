package cn.itsource.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * <p>
 * 用户和角色中间表
 * </p>
 *
 * @author zhaodi
 * @since 2024-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_login_role")
@Schema(name = "LoginRole对象", description = "用户和角色中间表")
public class LoginRole implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "loginId", description = "")
    @TableField("login_id")
    private Long loginId;

    @Schema(name = "roleId", description = "")
    @TableField("role_id")
    private Long roleId;

    @Schema(name = "id", description = "")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

}
