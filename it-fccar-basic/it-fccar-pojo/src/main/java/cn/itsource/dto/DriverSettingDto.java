package cn.itsource.dto;

import lombok.Data;

@Data
public class DriverSettingDto {
    //接单距离
    private Integer orderDistance;
    //接单条件
    private Integer rangeDistance;
}
