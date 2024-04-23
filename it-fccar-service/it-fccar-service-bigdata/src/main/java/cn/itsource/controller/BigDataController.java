package cn.itsource.controller;

import cn.hutool.core.util.IdUtil;
import cn.itsource.dto.DriverPoint;
import cn.itsource.pojo.UploadLocationParam;
import cn.itsource.result.R;
import cn.itsource.service.BigDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/bigdata")
public class BigDataController {
    @Resource
    private BigDataService bigDataService;
    @PostMapping("/uploadLocation")
    public R<Boolean> uploadLocation(@RequestBody UploadLocationParam param) {
        DriverPoint driverPoint = new DriverPoint();
        long id = IdUtil.getSnowflake().nextId();
        driverPoint.setId(id);
        BeanUtils.copyProperties(param, driverPoint);
        bigDataService.savePoint(driverPoint);
        return R.success();
    }
}
