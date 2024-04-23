package cn.itsource.template;

import cn.itsource.config.ParamsConfig;
import cn.itsource.constants.GlobalExceptionCode;
import cn.itsource.pojo.dto.PhoneResultInfo;
import cn.itsource.pojo.dto.ReqCode;
import cn.itsource.utils.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class WechatLoginTemplate {

    private final ParamsConfig properties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();



    public WechatLoginTemplate(ParamsConfig properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    //微信登录：获取OpenId
    public String getWxOpenId(String wxCode) {
        // 从配置中获取微信Secret
        String wxSecret = properties.getWxSecret();
        // 从配置中获取微信AppID
        String wxAppid = properties.getWxAppid();
        // 拼接请求URL
        String url = String.format(properties.getUrl(), wxAppid, wxSecret, wxCode);
        // 发起GET请求获取响应
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        HttpStatus statusCode = responseEntity.getStatusCode();
        AssertUtil.isTrue(statusCode == HttpStatus.OK, GlobalExceptionCode.WECHAT_CODE_ERROR);
        // 获取响应体
        String responseData = responseEntity.getBody();
        // 解析响应体为JSON对象
        JSONObject parseData = JSON.parseObject(responseData);
        // 返回微信OpenID
        return parseData.getString("openid");
    }



    //微信登录：获取accessTocken
    public String getAccessToken(){
        String url = String.format(properties.getAccessUrl(), properties.getWxAppid(), properties.getWxSecret());
        ResponseEntity<String> accessToken = restTemplate.getForEntity(url, String.class);
        HttpStatus statusCode = accessToken.getStatusCode();
        AssertUtil.isTrue(statusCode == HttpStatus.OK, GlobalExceptionCode.WECHAT_CODE_ERROR);
        // 获取响应体
        String responseData = accessToken.getBody();
        // 解析响应体为JSON对象
        JSONObject parseData = JSON.parseObject(responseData);
        // 返回微信OpenID
        return parseData.getString("access_token");
    }

    //微信登录，获取手机号
    public String getPhoneNumber(String phoneCode) throws JsonProcessingException {
        // 创建一个req对象
        ReqCode code = new ReqCode();
        code.setCode(phoneCode);
        // 使用ObjectMapper将User对象转换为JSON字符串
        String jsonUser = objectMapper.writeValueAsString(code);
        // 创建HttpHeaders和HttpEntity对象
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonUser, headers);
        String url = String.format(properties.getPhoneUrl(),getAccessToken());
        // 发送POST请求
        ResponseEntity<String> response = restTemplate.postForEntity(
                url, // 替换为实际的目标URL
                entity,
                String.class
        );
        PhoneResultInfo phoneResultInfo = JSON.parseObject(response.getBody(), PhoneResultInfo.class);

        return phoneResultInfo.getPhoneInfo().getPurePhoneNumber();
    }
}
