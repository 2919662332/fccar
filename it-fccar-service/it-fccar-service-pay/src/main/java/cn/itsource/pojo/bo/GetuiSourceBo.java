package cn.itsource.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetuiSourceBo {
    private String alias;
    private Object content;
    private Integer status;
    private String PayOrderNo;
}
