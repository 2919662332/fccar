package cn.itsource.template;

import cn.itsource.config.AuthConfig;
import cn.itsource.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthTemplate {

    private AuthConfig authConfig;

    public AuthTemplate(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    /**
     * 身份证二要素校验
     * @param name
     * @param idNum
     * @return
     */
    public Boolean AuthStatus(String name, String idNum) {
        //身份证核验
        String host = authConfig.getHost();
        String path = authConfig.getPath();
        String method = authConfig.getMethod();
        String appcode = authConfig.getAppcode();
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("idCardNo", idNum);
        bodys.put("name", name);
        Boolean success = null;
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            JSONObject parseObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
            success = (Boolean) parseObject.get("success");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }


    /**
     * 图片校验
     * @param imgUrl
     * @return
     */
    public Boolean ImgAuthStatus(String imgUrl){
        String host = "https://jmtxshjcsh.market.alicloudapi.com";
        String path = "/image-review/generic";
        String method = "POST";
        String appcode = "63264477ef814fe8ab7b28250becdb72";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("imgUrl", imgUrl);
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            JSONObject parseObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
            // 获取data中的result字段
            JSONObject data = parseObject.getJSONObject("data");
            log.info("data: {}" ,data);
            int result = data.getIntValue("result");
            return result == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
