package cn.itsource.service.impl;

import cn.itsource.pojo.domain.CustomerSummary;
import cn.itsource.mapper.CustomerSummaryMapper;
import cn.itsource.service.ICustomerSummaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 乘客数据汇总 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-04-01
 */
@Service
public class CustomerSummaryServiceImpl extends ServiceImpl<CustomerSummaryMapper, CustomerSummary> implements ICustomerSummaryService {

    @Override
    public void saveSummary(Long id) {
        CustomerSummary customerSummary = new CustomerSummary();
        customerSummary.setId(id);
        super.save(customerSummary);
    }
}
