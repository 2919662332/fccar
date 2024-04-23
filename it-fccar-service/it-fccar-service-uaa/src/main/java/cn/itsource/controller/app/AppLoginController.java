package cn.itsource.controller.app;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.itsource.domian.LoginSuccess;
import cn.itsource.pojo.domain.LoginInfo;
import cn.itsource.result.R;
import cn.itsource.service.ILoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/app/mp")
public class AppLoginController {

    @Resource
    private ILoginService service;

    @PostMapping("/login")
    @SaIgnore
    public R<LoginSuccess> login(@RequestBody @Valid LoginInfo loginInfo) {
        return R.success(service.login(loginInfo));
    }
}
