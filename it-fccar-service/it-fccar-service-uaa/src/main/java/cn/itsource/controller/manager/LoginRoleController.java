package cn.itsource.controller.manager;

import cn.itsource.service.ILoginRoleService;
import cn.itsource.pojo.domain.LoginRole;
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

@Tag(name = "用户和角色中间表",description = "用户和角色中间表")
@RestController
@RequestMapping("/manager/loginRole")
public class LoginRoleController{

    @Autowired
    public ILoginRoleService loginRoleService;

    @Operation( summary= "保存LoginRole",description = "基础对象保存接口")
    @Parameter(name = "loginRole",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid LoginRole loginRole){
        return R.success(loginRoleService.save(loginRole));
    }

    @Operation( summary= "修改LoginRole",description = "基础对象修改接口")
    @Parameter(name = "loginRole",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid LoginRole loginRole){
        return R.success(loginRoleService.updateById(loginRole));
    }

    //删除对象
    @Operation( summary= "删除LoginRole",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(loginRoleService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取LoginRole",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<LoginRole> get(@PathVariable("id")Long id){
        return R.success(loginRoleService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询LoginRole列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<LoginRole>> list(@RequestBody PageQueryWrapper<LoginRole> query){
        QueryWrapper<LoginRole> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(loginRoleService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询LoginRole分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<LoginRole>> page(@RequestBody PageQueryWrapper<LoginRole> query){
        //分页查询
        Page<LoginRole> page = new Page<LoginRole>(query.getPage(),query.getRows());
        QueryWrapper<LoginRole> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = loginRoleService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<LoginRole>(page.getTotal(),page.getRecords()));
    }

}
