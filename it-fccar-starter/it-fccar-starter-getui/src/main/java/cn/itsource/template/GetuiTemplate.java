package cn.itsource.template;

import cn.itsource.config.ConfigParam;
import cn.itsource.exception.GlobalException;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
@Slf4j
public class GetuiTemplate {
    private final ConfigParam configParam;
    public GetuiTemplate(ConfigParam configParam) {
        this.configParam = configParam;
    }

    public void goBindCid(String alias,String message) {

        // 设置httpClient最大连接数
        System.setProperty(configParam.getSetMaxConncetSetting(), configParam.getSetMaxConnectNum());
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        //填写应用配置
        apiConfiguration.setAppId(configParam.getAppid());
        apiConfiguration.setAppKey(configParam.getAppkey());
        apiConfiguration.setMasterSecret(configParam.getMasterSecret());
        // 接口调用前缀
        apiConfiguration.setDomain(configParam.getDomain());
        // 实例化ApiHelper对象，用于创建接口对象
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        //1.创建消息推送API
        PushApi pushApi = apiHelper.creatApi(PushApi.class);

        //根据cid进行单推
        PushDTO<Audience> pushDTO = new PushDTO<>();
        // 设置推送参数
        pushDTO.setRequestId(System.currentTimeMillis() + "");

        /**** 设置个推通道参数 *****/
        PushMessage pushMessage = new PushMessage();
        pushMessage.setTransmission(message);
        pushDTO.setPushMessage(pushMessage);

        /*设置接收人信息*/
        Audience audience = new Audience();
        pushDTO.setAudience(audience);
        audience.setAlias(Arrays.asList(alias));
        // 进行cid单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByAlias(pushDTO);

        if (!apiResult.isSuccess()){
            throw new GlobalException("推送异常，请重试");
        }

    }
}
