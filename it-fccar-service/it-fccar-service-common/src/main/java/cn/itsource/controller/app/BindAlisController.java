package cn.itsource.controller.app;

import cn.itsource.result.R;
import cn.itsource.service.IBindAliService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/common")
public class BindAlisController {

    @Resource
    private IBindAliService bindAliService;
    @PostMapping("/BindCidAlis/{cid}")
    public R<?> BindCidAlis(@PathVariable String cid) {
        bindAliService.goBindCid(cid);
        return R.success();
    }
}
