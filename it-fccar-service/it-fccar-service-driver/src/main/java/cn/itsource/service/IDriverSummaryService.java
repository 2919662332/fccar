package cn.itsource.service;

import cn.itsource.pojo.domain.DriverSummary;
import cn.itsource.pojo.dto.RealAuthStatus;
import cn.itsource.result.R;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 司机结算对象 服务类
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
public interface IDriverSummaryService extends IService<DriverSummary> {

    void save(long id);

}
