package cn.itsource.service;

import cn.itsource.bo.DriverGeo;
import cn.itsource.dto.DriverSettingDto;
import cn.itsource.pojo.domain.Driver;
import cn.itsource.dto.LocalParams;
import cn.itsource.pojo.dto.RealAuthStatus;
import cn.itsource.pojo.dto.RegisterDto;
import cn.itsource.result.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * <p>
 * 司机对象 服务类
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
public interface IDriverService extends IService<Driver> {

    Driver save(RegisterDto registerDto) throws JsonProcessingException;

    RealAuthStatus getRealAuthSuccess();

    R<DriverSettingDto> saveSetting();

    void saveGeo(DriverGeo driverGeo);

    void stopAcceptOrder();

    void location(LocalParams localParams);
}
