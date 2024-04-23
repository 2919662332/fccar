package cn.itsource.service;

import cn.itsource.pojo.domain.CustomerSummary;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 乘客数据汇总 服务类
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
public interface ICustomerSummaryService extends IService<CustomerSummary> {

    void saveSummary(Long id);
}
