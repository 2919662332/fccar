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
 * 客户车辆
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer_car")
@Schema(name = "CustomerCar对象", description = "客户车辆")
public class CustomerCar implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "主键")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "customerId", description = "客户ID")
    @TableField("customer_id")
    private Long customerId;

    @Schema(name = "carPlate", description = "车牌号")
    @TableField("car_plate")
    private String carPlate;

    @Schema(name = "carType", description = "车型")
    @TableField("car_type")
    private String carType;

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

}
