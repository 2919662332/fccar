package cn.itsource;

import cn.hutool.core.util.IdUtil;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayApplication.class)
@Slf4j
public class WxPayTest {

    /** 商户号 */
    public static String merchantId = "1637705483";
    public static String appid = "wx2b12f5643aff0fc1";


    /** 商户API私钥路径 */
    public static String privateKeyPath = "D:\\itsource\\data\\apiclient_key.pem";
    /** 商户证书序列号 */
    public static String merchantSerialNumber = "1B5FCE6A83E2CD2F55441857E978F8AF7B8D85F4";
    /** 商户APIV3密钥 */
    public static String apiV3Key = "KkaXVb5fYsGqN998GS8pqoCVghhrpoW6";

    public static JsapiServiceExtension service;

    @Test
    public void test001() {
        // 初始化商户配置
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(merchantId)
                        // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                        .privateKeyFromPath(privateKeyPath)
                        .merchantSerialNumber(merchantSerialNumber)
                        .apiV3Key(apiV3Key)
                        .build();
        // 初始化服务
        service =
                new JsapiServiceExtension.Builder()
                        .config(config)
                        .signType("RSA") // 不填默认为RSA
                        .build();
        try {
            // ... 调用接口
            PrepayWithRequestPaymentResponse response = prepayWithRequestPayment();
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /** 关闭订单 */
    public static void closeOrder() {

        CloseOrderRequest request = new CloseOrderRequest();
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        // 调用接口
        service.closeOrder(request);
    }
    /** JSAPI支付下单，并返回JSAPI调起支付数据 */
    public static PrepayWithRequestPaymentResponse prepayWithRequestPayment() {
        PrepayRequest request = new PrepayRequest();

        Payer payer = new Payer();
        payer.setOpenid("obPAQ5iOrpSuUG5Ru0malld39rvE");
        request.setPayer(payer);
        Amount amount = new Amount();
        amount.setTotal(1);
        request.setAmount(amount);
        request.setAppid(appid);
        request.setMchid(merchantId);
        request.setDescription("测试商品标题");
        request.setNotifyUrl("https://notify_url");
        request.setOutTradeNo(IdUtil.getSnowflake(1,3).nextIdStr());
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        // 调用接口
        return service.prepayWithRequestPayment(request);
    }
}
