package cn.itsource.service;

import cn.itsource.pojo.domain.DriverSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 司机配置 服务类
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
public interface IDriverSettingService extends IService<DriverSetting> {



    void save(long id);
}
