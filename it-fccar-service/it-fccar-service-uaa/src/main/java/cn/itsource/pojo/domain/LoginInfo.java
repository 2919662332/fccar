package cn.itsource.pojo.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginInfo {
    @NotNull(message = "wxCodes不能为空")
    private String wxCode;
    @NotNull(message = "非法参数")
    private Integer type;
}
