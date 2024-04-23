package cn.itsource.controller.app;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.itsource.pojo.domain.CustomerCar;
import cn.itsource.pojo.dto.WxCodeDto;
import cn.itsource.result.R;
import cn.itsource.service.ICustomerCarService;
import cn.itsource.service.ICustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "app客户操作",description = "客户操作")
@RestController
@RequestMapping("/app/customer")
public class AppCostumerController {

    @Resource
    private ICustomerService customerService;
    @Resource
    private ICustomerCarService customerCarService;

    @PostMapping("/register")
    @Operation( summary= "乘客注册",description = "基础乘客注册")
    @SaIgnore
    public R register(@RequestBody @Valid WxCodeDto wxCodeDto) throws JsonProcessingException {
        return customerService.register(wxCodeDto);
    }

    @GetMapping("/car/list/current")
    @Operation( summary= "代驾车辆",description = "代驾车辆查询")
    @SaIgnore
    public R selectCar() {
        List<CustomerCar> carList = customerCarService.selectCar();
        return R.success(carList);
    }
    @PostMapping("/car/add")
    @Operation( summary= "车辆添加",description = "车辆添加")
    @SaIgnore
    public R addCar(@RequestBody @Valid CustomerCar customerCar) {
        customerCarService.addCar(customerCar);
        return R.success();
    }
}
