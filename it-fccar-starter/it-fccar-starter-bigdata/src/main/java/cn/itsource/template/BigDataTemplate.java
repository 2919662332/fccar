package cn.itsource.template;

import cn.itsource.config.TencentMapParams;
import cn.itsource.exception.GlobalException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
@Slf4j
public class BigDataTemplate {
    private final TencentMapParams tencentMapParams;

    private final RestTemplate restTemplate;

    public BigDataTemplate(TencentMapParams tencentMapParams,RestTemplate restTemplate) {
        this.tencentMapParams = tencentMapParams;
        this.restTemplate = restTemplate;
    }

    public Integer calRealMileage(String from,String to) {
        String format = String.format(tencentMapParams.getUrl(), from, to, tencentMapParams.getMapKey());
        ResponseEntity<String> forEntity = restTemplate.getForEntity(format, String.class);
        String responseData = forEntity.getBody();
        JSONObject parseObject = JSON.parseObject(responseData);
        log.info("parseObject: " + parseObject);
        Integer status = parseObject.getInteger("status");
        if (status != 0) {
            throw new GlobalException("计算真实里程失败！");
        }
        Integer distance = parseObject.getJSONObject("result")
                .getJSONArray("rows")
                .getJSONObject(0)
                .getJSONArray("elements")
                .getJSONObject(0)
                .getInteger("distance");

        return distance / 1000;
    }
}
