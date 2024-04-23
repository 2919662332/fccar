package cn.itsource.intercepter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class TokenInterceptor implements RequestInterceptor {

    @Value("sa-token.token-name")
    private String tokenName;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String tokenValue = requestAttributes.getRequest().getHeader(tokenName);
        if(StringUtils.isNotEmpty(tokenValue)){
            requestTemplate.header(tokenName,tokenValue);
            log.warn("token转发 - {}" , tokenValue);
        }else{
            log.warn("token转发 - 请求头中并没有Token");
        }

    }
}