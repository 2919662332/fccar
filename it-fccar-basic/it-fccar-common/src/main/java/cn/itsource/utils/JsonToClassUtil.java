package cn.itsource.utils;


import cn.hutool.json.JSONUtil;

/**
 * JSON转换工具
 */
public class JsonToClassUtil {

    /**
     * 使用hutool工具把Obj类型对象转换为指定的对象类型
     * @param obj
     * @param cls
     * @return
     * @param <T>
     */
    public static <T> T object2Class(Object obj, Class<T> cls){
        return JSONUtil.toBean(JSONUtil.toJsonStr(obj), cls);
    }

    /**
     * 使用hutool工具把字符串Json类型转换为指定的对象类型
     * @param jsonObjStr
     * @param cls
     * @return
     * @param <T>
     */
    public static <T> T objJsonStr2Class(String jsonObjStr, Class<T> cls){
        return JSONUtil.toBean(jsonObjStr, cls);
    }


}
