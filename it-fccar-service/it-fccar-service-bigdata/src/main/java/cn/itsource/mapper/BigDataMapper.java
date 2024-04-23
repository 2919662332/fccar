package cn.itsource.mapper;

import cn.itsource.dto.DriverPoint;
import java.util.List;

public interface BigDataMapper {
    void savePoint(DriverPoint point);

    List<DriverPoint> selectPoint(String orderNo);
}
