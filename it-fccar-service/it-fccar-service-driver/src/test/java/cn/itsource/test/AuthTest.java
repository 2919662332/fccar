package cn.itsource.test;

import cn.itsource.DriverApplication;
import cn.itsource.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = DriverApplication.class)
public class AuthTest {

    @Test
    public void test() {
        String host = "https://jmtxshjcsh.market.alicloudapi.com";
        String path = "/image-review/generic";
        String method = "POST";
        String appcode = "63264477ef814fe8ab7b28250becdb72";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("imgUrl", "https://th.bing.com/th/id/R.4e379b6d4c0f752655095dac8e6a0fe0?rik=nfCu6OW1zuIxzA&pid=ImgRaw&r=0");
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            JSONObject parseObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));

            // 获取data中的result字段
            JSONObject data = parseObject.getJSONObject("data");
            int result = data.getIntValue("result");
            // 根据result字段的值进行判断
            switch (result) {
                case 1 -> System.out.println("图片合规");
                case 2 -> System.out.println("图片不合规");
                case 3 -> System.out.println("图片疑似");
                case 4 -> System.out.println("图片审核失败");
                default -> System.out.println("未知的审核结果");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
