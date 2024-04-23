package cn.itsource.controller.app;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.itsource.bo.DriverGeo;
import cn.itsource.pojo.domain.Driver;
import cn.itsource.pojo.domain.DriverAuthMaterial;
import cn.itsource.dto.LocalParams;
import cn.itsource.pojo.dto.RealAuthStatus;
import cn.itsource.pojo.dto.RegisterDto;
import cn.itsource.result.R;
import cn.itsource.service.IDriverAuthMaterialService;
import cn.itsource.service.IDriverService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/app/driver")
@Tag(name = "app-driver", description = "app-driver")
public class AppDriverController {
    @Resource
    private IDriverService driverService;
    @Resource
    private IDriverAuthMaterialService authMaterialService;

    //司机注册
    @PostMapping("/register")
    @SaIgnore
    public R<Driver> register(@RequestBody @Valid RegisterDto registerDto) throws JsonProcessingException {
        return R.success(driverService.save(registerDto));
    }

    //司机提交实名材料
    @PostMapping("/submit/AuthMaterials")
    @SaIgnore
    public R<DriverAuthMaterial> submitAuthMaterials(@RequestBody @Valid DriverAuthMaterial authMaterial) {

        return authMaterialService.submitAuthMaterials(authMaterial);
    }

    //司机实名材料回显
    @PostMapping("/dbDate")
    public R<DriverAuthMaterial> dbDate(){
        return authMaterialService.queryEchoData();
    }
    //获取是否实名完成以及今日接单数据
    @GetMapping("/getRealAuthSuccess")
    public R<RealAuthStatus> getRealAuthSuccess(){
        return R.success(driverService.getRealAuthSuccess());
    }
    //保存司机配置到redis
    @PostMapping("/saveSetting")
    public R saveSetting(){
        driverService.saveSetting();
        return R.success();
    }
    //保存司机配置到redis
    @PostMapping("/geo")
    public R<DriverGeo> saveGeo(@RequestBody @Valid DriverGeo driverGeo){
        driverService.saveGeo(driverGeo);
        return R.success();
    }
    //stopAcceptOrder
    @PostMapping("/stopAcceptOrder")
    public R stopAcceptOrder(){
        driverService.stopAcceptOrder();
        return R.success();
    }
    //cache/location
    @PostMapping("/cache/location")
    public R<Boolean> location(@RequestBody LocalParams localParams){
        driverService.location(localParams);
        return R.success();
    }
}
