package cn.itsource.exception;

import cn.itsource.constants.GlobalExceptionCode;
import cn.itsource.utils.AssertUtil;
import lombok.Data;

/**
 * 自定义业务异常
 */
@Data
public class GlobalException extends RuntimeException{

    private Integer code;
    private String msg;

    public GlobalException(String message){
        super(message);
        this.msg = message;
        this.code = GlobalExceptionCode.SERVICE_ERROR.getCode();
    }

    public GlobalException(Integer code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public GlobalException(GlobalExceptionCode globalExceptionCode){
        super(globalExceptionCode.getMessage());
        this.code = globalExceptionCode.getCode();
        this.msg = globalExceptionCode.getMessage();
    }

}
