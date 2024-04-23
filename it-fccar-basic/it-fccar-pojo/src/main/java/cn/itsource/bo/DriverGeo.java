package cn.itsource.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverGeo {
    private Double latitude;
    private Double longitude;
}
