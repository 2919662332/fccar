package cn.itsource.service.impl;

import cn.itsource.pojo.domain.DriverSetting;
import cn.itsource.mapper.DriverSettingMapper;
import cn.itsource.service.IDriverSettingService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 司机配置 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
@Service
public class DriverSettingServiceImpl extends ServiceImpl<DriverSettingMapper, DriverSetting> implements IDriverSettingService {

    @Override
    public void save(long id) {
        DriverSetting driverSetting = new DriverSetting();
        driverSetting.setId(id);
        driverSetting.setAutoAccept(true);
        driverSetting.setOrientation(true);
        driverSetting.setListenService(true);
        driverSetting.setOrderDistance(50);
        driverSetting.setRangeDistance(20);
        driverSetting.setCreateTime(new Date());
        super.save(driverSetting);
    }
}
