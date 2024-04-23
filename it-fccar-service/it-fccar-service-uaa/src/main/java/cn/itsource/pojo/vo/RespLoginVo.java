package cn.itsource.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespLoginVo {
    private Long id;
    private String phone;
    private Boolean admin;
    private String nickName;
    private String avatar;
}
