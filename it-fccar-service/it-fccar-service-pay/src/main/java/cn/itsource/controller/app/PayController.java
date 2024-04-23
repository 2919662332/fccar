package cn.itsource.controller.app;

import cn.itsource.dto.WxPayParams;
import cn.itsource.result.R;
import cn.itsource.service.IPayService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/pay")
public class PayController {

    @Resource
    private IPayService payService;

    /**
     * 微信支付的调用
     * @param orderNo
     * @return
     */
    @PostMapping("/wxmp/{orderNo}")
    public R<WxPayParams> wxPay(@PathVariable String orderNo) {
        WxPayParams wxPayParams = payService.wxPay(orderNo);
        return R.success(wxPayParams);
    }
}
