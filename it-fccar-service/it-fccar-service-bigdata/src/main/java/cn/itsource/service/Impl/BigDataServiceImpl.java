package cn.itsource.service.Impl;

import cn.itsource.dto.DriverPoint;
import cn.itsource.mapper.BigDataMapper;
import cn.itsource.service.BigDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BigDataServiceImpl implements BigDataService {
    @Resource
    private BigDataMapper bigDataMapper;
    @Override
    public void savePoint(DriverPoint point) {
        bigDataMapper.savePoint(point);
    }

    @Override
    public List<DriverPoint> selectPoint(String orderNo) {
        return bigDataMapper.selectPoint(orderNo);
    }
}
