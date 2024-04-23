package cn.itsource.controller.manager;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.itsource.service.ILoginService;
import cn.itsource.pojo.domain.Login;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.itsource.pojo.query.PageQueryWrapper;
import cn.itsource.result.R;
import java.util.List;
import cn.itsource.result.PageResult;

@Tag(name = "登录表",description = "登录表")
@RestController
@RequestMapping("/manager/login")
public class LoginController{

    @Autowired
    public ILoginService loginService;

    @Operation( summary= "保存Login",description = "基础对象保存接口")
    @Parameter(name = "login",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid Login login){
        return R.success(loginService.save(login));
    }

    @Operation( summary= "修改Login",description = "基础对象修改接口")
    @Parameter(name = "login",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid Login login){
        return R.success(loginService.updateById(login));
    }

    //删除对象
    @Operation( summary= "删除Login",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(loginService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取Login",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    @SaCheckPermission("LOGIN:GET")
    public R<Login> get(@PathVariable("id")Long id){
        return R.success(loginService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询Login列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<Login>> list(@RequestBody PageQueryWrapper<Login> query){
        QueryWrapper<Login> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(loginService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询Login分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<Login>> page(@RequestBody PageQueryWrapper<Login> query){
        //分页查询
        Page<Login> page = new Page<Login>(query.getPage(),query.getRows());
        QueryWrapper<Login> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = loginService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<Login>(page.getTotal(),page.getRecords()));
    }

}
