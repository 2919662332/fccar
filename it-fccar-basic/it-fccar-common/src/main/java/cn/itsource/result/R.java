package cn.itsource.result;

import cn.itsource.constants.GlobalExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//返回JSON结果
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {

    //结果状态
    private boolean success = true;
    //响应码
    private int code = GlobalExceptionCode.OK.getCode();
    //默认消息
    private String message = GlobalExceptionCode.OK.getMessage();
    //返回的数据
    private T data;

    /**
     * 创建无参数的成功的JSON结果对象
     * @return R:JSON结果对象
     */
    public static R success(){
        return new R();
    }

    /**
     * 创建成功的JSON结果对象
     * @return R:JSON结果对象
     * @param data: 数据
     */
    public static <T> R success(T data){
        R<T> r = new R<>();
        r.setData(data);
        return r;
    }

    /**
     * 创建成功的JSON结果对象
     * @return R:JSON结果对象
     * @param code: 响应码
     * @param data: 数据
     */
    public static <T> R success(int code,T data){
        R r = success(data);
        r.setCode(code);
        return r;
    }
    /**
     * 创建失败的JSON结果对象
     * @return R:JSON结果对象
     * @param code: 响应码
     * @param message: 错误信息
     */
    public static R error(int code,String message){
        return new R(false,code, message,null);
    }

    /**
     * 创建失败的JSON结果对象
     * @return R:JSON结果对象
     */
    public static R error(){
        return error(GlobalExceptionCode.SERVICE_ERROR.getCode(), GlobalExceptionCode.SERVICE_ERROR.getMessage());
    }
    /**
     * 创建失败的JSON结果对象
     * @return R:JSON结果对象
     */
    public static R error(String message){
        return error(GlobalExceptionCode.SERVICE_ERROR.getCode(), message);
    }

    //直接传递一个错误码
    public static R error(GlobalExceptionCode GlobalExceptionCode){
        return error(GlobalExceptionCode.getCode(),GlobalExceptionCode.getMessage());
    }

    public R<T> data(T data){
        this.data = data;
        return this;
    }
}
