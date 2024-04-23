package cn.itsource.remote.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SaveLoginParam {
    @NotNull(message = "ID不能为空")
    private Long id;
    private String username;
    @NotNull(message = "type不能为空")
    private Integer type;
    private Date createTime;
    @NotNull(message = "phone不能为空")
    private String phone;
    private String openId;
}
