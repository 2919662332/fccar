package cn.itsource.service;

import cn.itsource.domian.LoginSuccess;
import cn.itsource.pojo.domain.ClientRespInfo;
import cn.itsource.pojo.domain.Login;
import cn.itsource.pojo.domain.LoginInfo;
import cn.itsource.pojo.dto.ClientLoginInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * <p>
 * 登录表 服务类
 * </p>
 *
 * @author zhaodi
 * @since 2024-03-29
 */
public interface ILoginService extends IService<Login> {

    LoginSuccess login(LoginInfo loginInfo);

    ClientRespInfo clientLogin(ClientLoginInfo clientLoginInfo) throws JsonProcessingException;
}
