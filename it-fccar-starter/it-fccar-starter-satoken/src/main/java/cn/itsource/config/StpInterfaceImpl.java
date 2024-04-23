package cn.itsource.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.itsource.constants.Constants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
public class StpInterfaceImpl implements StpInterface {

    private RedisTemplate<String,Object> redisTemplate;


    public StpInterfaceImpl(RedisTemplate<String,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 返回一个账号所拥有的权限码集合 
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        String format = String.format(Constants.Redis.PERMISSION_LOGIN, loginId);
        List<String> permissionsList = (List<String>) redisTemplate.opsForValue().get(format);
        return permissionsList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<String>();    
        list.add("admin");
        list.add("super-admin");
        return null;
    }

}
