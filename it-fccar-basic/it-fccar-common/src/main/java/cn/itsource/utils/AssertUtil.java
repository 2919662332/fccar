package cn.itsource.utils;


import cn.itsource.constants.Constants;
import cn.itsource.constants.GlobalExceptionCode;
import cn.itsource.exception.GlobalException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//断言工具
public class AssertUtil {

    //手机的正则表达式
    public static void isPhone(String phone, GlobalExceptionCode globalExceptionCode){
        Pattern CHINA_PATTERN_PHONE = Pattern.compile(Constants.CHINA_PHONE_REGEX);
        Matcher m = CHINA_PATTERN_PHONE.matcher(phone);
        if(!m.matches()){
            throw new GlobalException(globalExceptionCode);
        }
    }


    //字符串不为空
    public static void isNotEmpty(String text, GlobalExceptionCode globalExceptionCode) {
        if (text == null || text.trim().length() == 0) {
            throw new GlobalException(globalExceptionCode);
        }
    }

    //列表不为空
    public static void isNotEmpty(List list, GlobalExceptionCode globalExceptionCode) {
        if (list == null || list.size() == 0) {
            throw new GlobalException(globalExceptionCode);
        }
    }

    //对象为空
    public static void isNull(Object obj , GlobalExceptionCode globalExceptionCode){
        if(obj != null){
            throw new GlobalException(globalExceptionCode);
        }
    }
    //对象不为空
    public static void isNotNull(Object obj , GlobalExceptionCode globalExceptionCode){
        if(obj == null){
            throw new GlobalException(globalExceptionCode);
        }
    }
    //断言为false
    public static void isFalse(boolean isFalse , GlobalExceptionCode globalExceptionCode){
        if(isFalse){
            throw new GlobalException(globalExceptionCode);
        }
    }
    //断言为false
    public static void isTrue(boolean isTrue , GlobalExceptionCode globalExceptionCode){
        if(!isTrue){
            throw new GlobalException(globalExceptionCode);
        }
    }
    //int值比较
    public static void isEquals(int s1,int s2 , GlobalExceptionCode globalExceptionCode){
        if(s1 != s2){
            throw new GlobalException(globalExceptionCode);
        }
    }
    //是否包含
    public static void isIn(Integer s1,List<Integer> s2 , GlobalExceptionCode globalExceptionCode){
        if(!s2.contains(s1)){
            throw new GlobalException(globalExceptionCode);
        }
    }
    //字符串比较
    public static void isEquals(String s1,String s2 , GlobalExceptionCode globalExceptionCode){
        isNotEmpty(s1, GlobalExceptionCode.PARAM_ERROR);
        isNotEmpty(s2, GlobalExceptionCode.PARAM_ERROR);
        if(!s1.equals(s2)){
            throw new GlobalException(globalExceptionCode);
        }
    }
    //字符串去空格比较
    public static void isEqualsTrim(String s1,String s2 , GlobalExceptionCode globalExceptionCode){
        isNotEmpty(s1, GlobalExceptionCode.PARAM_ERROR);
        isNotEmpty(s2, GlobalExceptionCode.PARAM_ERROR);
        if(!s1.trim().equals(s2.trim())){
            throw new GlobalException(globalExceptionCode);
        }
    }
    //字符串忽略大小写比较
    public static void isEqualsIgnoreCase(String s1,String s2 , GlobalExceptionCode globalExceptionCode){
        isNotEmpty(s1, GlobalExceptionCode.PARAM_ERROR);
        isNotEmpty(s2, GlobalExceptionCode.PARAM_ERROR);
        if(!s1.trim().equalsIgnoreCase(s2.trim())){
            throw new GlobalException(globalExceptionCode);
        }
    }

}
