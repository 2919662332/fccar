package cn.itsource.service.impl;

import cn.itsource.pojo.domain.DriverSummary;
import cn.itsource.mapper.DriverSummaryMapper;
import cn.itsource.pojo.dto.RealAuthStatus;
import cn.itsource.result.R;
import cn.itsource.service.IDriverSummaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 司机结算对象 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
@Service
public class DriverSummaryServiceImpl extends ServiceImpl<DriverSummaryMapper, DriverSummary> implements IDriverSummaryService {

    @Override
    public void save(long id) {
        DriverSummary driverSummary = new DriverSummary();
        driverSummary.setId(id);
        driverSummary.setCreateTime(new Date());
        super.save(driverSummary);
    }
}
