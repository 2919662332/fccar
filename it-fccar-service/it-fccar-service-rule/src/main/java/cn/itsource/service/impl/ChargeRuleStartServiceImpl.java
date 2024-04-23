package cn.itsource.service.impl;

import cn.itsource.dto.OrderPriceParamsDto;
import cn.itsource.pojo.domain.ChargeRuleStart;
import cn.itsource.mapper.ChargeRuleStartMapper;
import cn.itsource.pojo.bo.OrderPriceBo;
import cn.itsource.service.IChargeRuleStartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 计价规则-起步价 服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-04-10
 */
@Service
@Slf4j
public class ChargeRuleStartServiceImpl extends ServiceImpl<ChargeRuleStartMapper, ChargeRuleStart> implements IChargeRuleStartService {

}
