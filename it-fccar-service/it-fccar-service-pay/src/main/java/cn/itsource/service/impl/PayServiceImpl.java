package cn.itsource.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.itsource.domian.PayOrderDto;
import cn.itsource.exception.GlobalException;
import cn.itsource.dto.WxPayParams;
import cn.itsource.pojo.domain.PayOrder;
import cn.itsource.service.IPayOrderService;
import cn.itsource.service.IPayService;
import cn.itsource.template.WxPayTemplate;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
@Slf4j
public class PayServiceImpl implements IPayService {
    @Resource
    private WxPayTemplate wxPayTemplate;
    @Resource
    private IPayOrderService payOrderService;


    @Override
    public WxPayParams wxPay(String orderNo) {
        if (StringUtils.isEmpty(orderNo)) {
            throw new GlobalException("非法请求");
        }

        //查询支付单
        PayOrder payOrder = payOrderService.selectByorderNo(orderNo);

        if (payOrder == null) {
            throw new GlobalException("支付单不存在");
        }

        PayOrderDto payOrderDto = new PayOrderDto();
        payOrderDto.setPayOrderNo(payOrder.getPayOrderNo());
        payOrderDto.setSubject(payOrder.getSubject());
        payOrderDto.setAmount(payOrder.getAmount());
        //调用starter去接收发起支付的参数
        WxPayParams wxPayParams = wxPayTemplate.sendPayRequest(payOrderDto, StpUtil.getLoginIdAsLong());
        /* JSAPI支付下单，并返回JSAPI调起支付数据 */
        return wxPayParams;
    }
}
