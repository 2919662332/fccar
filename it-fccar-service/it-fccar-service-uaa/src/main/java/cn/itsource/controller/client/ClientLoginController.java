package cn.itsource.controller.client;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.itsource.pojo.domain.ClientRespInfo;
import cn.itsource.pojo.dto.ClientLoginInfo;
import cn.itsource.result.R;
import cn.itsource.service.ILoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/client/login")
public class ClientLoginController {

    @Resource
    private ILoginService loginService;

    @PostMapping("/admin")
    @SaIgnore
    public R login(@RequestBody ClientLoginInfo clientLoginInfo) throws JsonProcessingException {
        ClientRespInfo clientRespInfo = loginService.clientLogin(clientLoginInfo);
        return R.success(clientRespInfo);
    }
}
