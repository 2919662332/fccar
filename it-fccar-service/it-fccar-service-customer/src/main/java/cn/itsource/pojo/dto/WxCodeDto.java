package cn.itsource.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WxCodeDto {
    @NotNull(message = "wxcode不能为空")
    private String wxcode;
    private String phoneCode;
}
