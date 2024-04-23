package cn.itsource.service;

import cn.itsource.dto.DriverPoint;

import java.util.List;

public interface BigDataService {
    void savePoint(DriverPoint point);

    List<DriverPoint> selectPoint(String orderNo);
}
