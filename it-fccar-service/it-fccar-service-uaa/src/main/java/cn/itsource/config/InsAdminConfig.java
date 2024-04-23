package cn.itsource.config;

import cn.hutool.core.util.IdUtil;
import cn.itsource.pojo.domain.Login;
import cn.itsource.service.ILoginService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

@Component
public class InsAdminConfig {

    @Resource
    private ILoginService loginService;
    @Resource
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void initAdmin() {
        Login login = new Login();
        long id = IdUtil.getSnowflake(1, 5).nextId();
        login.setId(id);
        login.setUsername("admin");
        String encode = passwordEncoder.encode("123456");
        login.setPassword(encode); // 注意：这里应该使用更安全的方式来存储和验证密码
        login.setType(0);
        login.setEnabled(true);
        login.setAdmin(true);
        login.setNickName("叼毛的管理员");
        login.setCreateTime(new Date());
        // 检查管理员是否已经存在，如果不存在才添加
        if (!adminExists()) {
            loginService.save(login);
        }
    }
    private Boolean adminExists(){
        LambdaQueryWrapper<Login> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Login> eq = lambdaQueryWrapper.eq(Login::getUsername, "admin")
                .eq(Login::getAdmin, true);
        Login one = loginService.getOne(eq);
        return one != null;
    }
}
