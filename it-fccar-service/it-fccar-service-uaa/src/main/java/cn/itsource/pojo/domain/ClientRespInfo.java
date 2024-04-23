package cn.itsource.pojo.domain;

import cn.itsource.pojo.vo.RespLoginVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRespInfo implements Serializable {
    private RespLoginVo login;
    private String satoken;
    private List<String> dbPermissions = new ArrayList<>();
}
