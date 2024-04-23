package cn.itsource.controller.client;

import cn.itsource.pojo.dto.MaterialDto;
import cn.itsource.result.R;
import cn.itsource.service.IDriverAuthMaterialService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/client/realauth")
public class AuthMaterialController {

    @Resource
    private IDriverAuthMaterialService authMaterialService;

    @PostMapping("/audit")
    public R audit(@RequestBody MaterialDto materialDto) {
        return authMaterialService.auditMaterial(materialDto);
    }
    @PostMapping("/cancel")
    public R cancel(@RequestBody MaterialDto materialDto) {
        return authMaterialService.cancelAuditMaterial(materialDto);
    }
}
