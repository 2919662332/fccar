package cn.itsource.exception;

import cn.itsource.constants.Constants;
import cn.itsource.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 捕捉Exception异常，返回统一的错误提示
    @ExceptionHandler(value = Exception.class)
    public R exceptionErrorMethod(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        if(!e.getClass().getName().equals("NotLoginException")){

        }
        log.error("发生异常错误，请求地址：{}，异常信息{}", request.getRequestURI(), e.getMessage());
        return R.error(500, "服务器内部异常，请联系管理员");
    }

    // 捕捉GlobalException异常，把错误日志抛给用户 
    @ExceptionHandler(value = GlobalException.class)
    public R globalCustomMethod(GlobalException e, HttpServletRequest request) {
        e.printStackTrace();
        log.error("发生异常错误，请求地址：{}，异常信息：{}", request.getRequestURI(), e.getMessage());
        return R.error(e.getCode(), e.getMsg());
    }
}

