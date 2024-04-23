package cn.itsource.constants;

/**
 * 全局枚举错误码
 */
public enum GlobalExceptionCode {

    OK(200, "成功"),
    NO_PERMISSION(403, "没有访问权限"),
    PARAM_ERROR(400, "参数校验异常"),
    SERVICE_ERROR(500, "系统异常，我们正在殴打程序员"),
    SERVICE_REQUEST_ERROR(501, "远程服务调用异常"),
    PARAM_PHONE_ERROR(1000, "手机号错误"),
    PARAM_CODE_ERROR(1002, "授权码无效"),
    PARAM_PHONE_EXIST(1001, "账号已存在"),
    NAME_LENGTH_ERROR(6002, "姓名长度不够！"),
    WECHAT_REGISTER_ERROR(6003, "已注册！"),
    LOGIN_ERROR(401, "未注册"),
    PASSWORD_NO_MATCH(403, "密码不匹配"),
    WECHAT_CODE_ERROR(6004, "获取wxCode失败!"),
    ;
    //错误码
    private final int code;
    //错误信息
    private final String message;

    GlobalExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
