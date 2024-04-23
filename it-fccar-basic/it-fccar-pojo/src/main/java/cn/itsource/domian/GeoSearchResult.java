package cn.itsource.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoSearchResult {
    private String name;
    private Double latitude;
    private Double longitude;
    private Double distance;
}
