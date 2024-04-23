package cn.itsource.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.itsource.bo.PayOrderData;
import cn.itsource.constants.Constants;
import cn.itsource.mapper.PayOrderMapper;
import cn.itsource.pojo.domain.PayOrder;
import cn.itsource.service.IPayOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-04-19
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

    @Resource
    private PayOrderMapper payOrderMapper;

    @Override
    public PayOrder selectByorderNo(String orderNo) {
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>()
                .eq(PayOrder::getOrderNo,orderNo));
        return payOrder;
    }

    @Override
    public void create(PayOrderData payOrder) {

        //幂等，防止mq重复消费
        if (selectByorderNo(payOrder.getOrderNo()) != null)return;
        //保存支付单消息
        PayOrder dbPayOrder = new PayOrder();
        BeanUtils.copyProperties(payOrder, dbPayOrder);
        dbPayOrder.setCreateTime(new Date());
        dbPayOrder.setPayStatus(Constants.PayOrderStatus.UNPAY);
        dbPayOrder.setPayType(1);
        dbPayOrder.setPayOrderNo(IdUtil.getSnowflake().nextIdStr());
        super.save(dbPayOrder);
    }
}
