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
 * 
 * </p>
 *
 * @author zhaodi
 * @since 2024-04-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_systemdictionaryitem")
@Schema(name = "Systemdictionaryitem对象", description = "")
public class Systemdictionaryitem implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "parentId", description = "")
    @TableField("parent_id")
    private Long parentId;

    @Schema(name = "name", description = "")
    @TableField("name")
    private String name;

    @Schema(name = "requence", description = "排序")
    @TableField("requence")
    private Integer requence;

    @Schema(name = "intro", description = "")
    @TableField("intro")
    private String intro;

}
