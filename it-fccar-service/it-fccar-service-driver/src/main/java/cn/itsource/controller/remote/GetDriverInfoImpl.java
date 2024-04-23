package cn.itsource.controller.remote;

import cn.itsource.bo.DriverInfoBo;
import cn.itsource.exception.GlobalException;
import cn.itsource.pojo.domain.DriverSummary;
import cn.itsource.remote.api.DriverFeignApi;
import cn.itsource.result.R;
import cn.itsource.service.IDriverSummaryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class GetDriverInfoImpl implements DriverFeignApi {
    @Resource
    private IDriverSummaryService summaryService;

    /**
     * 获取司机当日的取消订单次数和投诉数
     * @param driverId
     * @return
     */
    @Override
    public R<DriverInfoBo>getDriverInfo(String driverId) {
        if (StringUtils.isEmpty(driverId)) {
            throw  new GlobalException("参数异常");
        }

        DriverSummary driverSummary = summaryService.getOne(new LambdaQueryWrapper<DriverSummary>()
                .eq(DriverSummary::getId, driverId));
        if (driverSummary == null){
            throw  new GlobalException("未查询到该信息");
        }
        DriverInfoBo driverInfoBo = new DriverInfoBo();
        driverInfoBo.setTodayCancel(driverSummary.getTodayCancel());
        driverInfoBo.setTodayComplaint(driverSummary.getTodayComplaint());
        return R.success(driverInfoBo);
    }
}
