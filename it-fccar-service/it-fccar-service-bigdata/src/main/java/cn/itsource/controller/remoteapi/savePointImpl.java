package cn.itsource.controller.remoteapi;

import cn.itsource.dto.DriverPoint;
import cn.itsource.remote.api.BigFeignApi;
import cn.itsource.result.R;
import cn.itsource.service.BigDataService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用于实现feign接口，保存和查询司机的实时坐标用于计算实际里程
 */
@RestController
public class savePointImpl implements BigFeignApi {
    @Resource
    private BigDataService bigDataService;
    @Override
    public R<Boolean> savePoint(DriverPoint point) {
        bigDataService.savePoint(point);
        return R.success();
    }

    @Override
    public R<List<DriverPoint>> selectPoint(String orderNo) {
        List<DriverPoint> driverPoints = bigDataService.selectPoint(orderNo);
        return R.success(driverPoints);
    }
}
