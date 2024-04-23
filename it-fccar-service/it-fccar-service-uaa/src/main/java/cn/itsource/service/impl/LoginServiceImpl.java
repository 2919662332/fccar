package cn.itsource.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.itsource.constants.Constants;
import cn.itsource.constants.GlobalExceptionCode;
import cn.itsource.domian.LoginSuccess;
import cn.itsource.exception.GlobalException;
import cn.itsource.mapper.LoginMapper;
import cn.itsource.pojo.domain.ClientRespInfo;
import cn.itsource.pojo.domain.Login;
import cn.itsource.pojo.domain.LoginInfo;
import cn.itsource.pojo.dto.ClientLoginInfo;
import cn.itsource.pojo.vo.RespLoginVo;
import cn.itsource.service.ILoginService;
import cn.itsource.template.WechatLoginTemplate;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 登录表 服务实现类
 * </p>
 *
 * @author zhaodi
 * @since 2024-03-29
 */
@Service
@Slf4j
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements ILoginService {
    @Resource
    private WechatLoginTemplate wechatLoginTemplate;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private LoginMapper loginMapper;
    @Override
    public LoginSuccess login(LoginInfo loginInfo) {
        String wxOpenId = wechatLoginTemplate.getWxOpenId(loginInfo.getWxCode());
        String key = String.format(Constants.Redis.DRIVER_OPENID, StpUtil.getLoginIdAsLong());
        redisTemplate.opsForValue().set(key,wxOpenId);

        LambdaQueryWrapper<Login> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Login> eq = lambdaQueryWrapper
                .eq(Login::getOpenId, wxOpenId)
                .eq(Login::getType, loginInfo.getType())
                .eq(Login::getEnabled, true);
        Login login = super.getOne(eq);
        if (login == null) {
            throw new GlobalException(GlobalExceptionCode.LOGIN_ERROR);
        }
        //查到了信息执行的逻辑
        StpUtil.login(login.getId());
        //去数据库查询权限列表
        List<String> list = loginMapper.selectPermissionByLoginId(login.getId());
        String format = String.format(Constants.Redis.PERMISSION_LOGIN, login.getId());
        redisTemplate.opsForValue().set(format,list);
        //把手机号，头像等返回给前端展示
        LoginSuccess loginSuccess = new LoginSuccess();
        BeanUtils.copyProperties(login,loginSuccess);
        //获取登录结果
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String tokenValue = tokenInfo.getTokenValue();
        //把Token返回给前端
        loginSuccess.setSatoken(tokenValue);
        //将登录信息放到redis中
        String loginInfos = String.format(Constants.Redis.USER_LOGIN_INFO, login.getId());
        redisTemplate.opsForValue().set(loginInfos,loginSuccess);
        return loginSuccess;
    }

    @Override
    public ClientRespInfo  clientLogin(ClientLoginInfo clientLoginInfo) {
        String username = clientLoginInfo.getUsername();
        String password = clientLoginInfo.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            throw new GlobalException("账号密码不能为空！");
        }
        //查询用户
        Login loginFromDB = super.getOne(new LambdaQueryWrapper<Login>().eq(Login::getUsername, clientLoginInfo.getUsername()));
        if (loginFromDB == null){
            throw new GlobalException(GlobalExceptionCode.LOGIN_ERROR);
        }
        //比较密码
        boolean passIsOk = passwordEncoder.matches(clientLoginInfo.getPassword(),loginFromDB.getPassword());
        if (!passIsOk){
            throw new GlobalException(GlobalExceptionCode.PASSWORD_NO_MATCH);
        }
        log.info("登录信息：{}",loginFromDB);
        //new对象保存要返回给前台的信息
        ClientRespInfo clientRespInfo = new ClientRespInfo();
        //替换redis的key
        String format = String.format(Constants.Redis.PERMISSION_LOGIN, loginFromDB.getId());
        //获取需要返回给前台的权限
        List<String> permissionsList = (List<String>) redisTemplate.opsForValue().get(format);
        //设置权限
        clientRespInfo.setDbPermissions(permissionsList);
        //执行登录生产satoken
        StpUtil.login(loginFromDB.getId());
        //获取satoken
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String tokenValue = tokenInfo.getTokenValue();
        //设置satoken
        clientRespInfo.setSatoken(tokenValue);
        //设置前台需要的信息
        clientRespInfo.setLogin(new RespLoginVo(loginFromDB.getId(),loginFromDB.getPhone(),loginFromDB.getAdmin(),loginFromDB.getNickName(),loginFromDB.getAvatar()));
        return clientRespInfo;
    }
}
