package cn.itsource.domian;

import lombok.Data;

@Data
public class LoginSuccess {
    private String name;
    private String satoken;
    private String phone;
    private String nickName;
    private String avatar;
}
