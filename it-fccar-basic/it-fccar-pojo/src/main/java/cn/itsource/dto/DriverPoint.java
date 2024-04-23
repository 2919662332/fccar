package cn.itsource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverPoint {
    private Long id;
    private String latitude;
    private String longitude;
    private Long driverId;
    private String orderNo;
}