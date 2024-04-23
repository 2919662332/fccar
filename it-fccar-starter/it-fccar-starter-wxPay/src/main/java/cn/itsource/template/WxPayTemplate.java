package cn.itsource.template;

import cn.itsource.config.WxPayConfig;
import cn.itsource.constants.Constants;
import cn.itsource.domian.PayOrderDto;
import cn.itsource.dto.WxPayParams;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class WxPayTemplate {
    private WxPayConfig wxPayConfig;
    private RedisTemplate<String,Object> redisTemplate;
    public static JsapiServiceExtension service;

    public WxPayTemplate(WxPayConfig wxPayConfig,RedisTemplate<String,Object> redisTemplate) {
        this.wxPayConfig = wxPayConfig;
        this.redisTemplate = redisTemplate;
    }

    public WxPayParams sendPayRequest(PayOrderDto payOrder, Long costumerId){
        //调用微信支付的后台接口获取拉取微信支付的参数
        // 初始化商户配置
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxPayConfig.getMerchantId())
                //使用com.wechat.pay.java.core.util中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                .privateKeyFromPath(wxPayConfig.getPrivateKeyPath())
                .merchantSerialNumber(wxPayConfig.getMerchantSerialNumber())
                .apiV3Key(wxPayConfig.getApiV3Key())
                .build();
        // 初始化服务
        service = new JsapiServiceExtension.Builder()
                .config(config)
                .signType("RSA")
                .build();
        WxPayParams wxPayParams = new WxPayParams();
        try {
            // ... 调用接口
            PrepayWithRequestPaymentResponse response = prepayWithRequestPayment(payOrder,costumerId);
            //将response中的信息提取出来返回给前台
            wxPayParams.setTimestamp(response.getTimeStamp());
            wxPayParams.setNonceStr(response.getNonceStr());
            wxPayParams.setPackageVal(response.getPackageVal());
            wxPayParams.setSignType(response.getSignType());
            wxPayParams.setPaySign(response.getPaySign());
            log.info("调用微信支付的接口获取的参数:{}",response);
        }catch (Exception e){
            e.printStackTrace();
        }
        return wxPayParams;
    }



    public PrepayWithRequestPaymentResponse prepayWithRequestPayment(PayOrderDto payOrder,Long costumerId) {
//        String key = String.format(Constants.Redis.DRIVER_OPENID, costumerId);

//        String openId =(String) redisTemplate.opsForValue().get(key);
        PrepayRequest request = new PrepayRequest();
        Payer payer = new Payer();
        //正确的写法因该去redis获取，但是由于没有商户号。所以...写死一个能用的
        payer.setOpenid("obPAQ5iOrpSuUG5Ru0malld39rvE");
        request.setPayer(payer);

        Amount amount = new Amount();
        amount.setTotal(payOrder.getAmount().intValue());
        request.setAmount(amount);
        request.setAppid(wxPayConfig.getAppid());
        request.setMchid(wxPayConfig.getMerchantId());
        request.setDescription(payOrder.getSubject());
        //微信的回调方法
        request.setNotifyUrl(wxPayConfig.getNotifyUrl());
        //交易单号，采用支付单号
        request.setOutTradeNo(payOrder.getPayOrderNo());
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        // 调用接口
        return service.prepayWithRequestPayment(request);
    }
}
