package cn.itsource.controller.manager;

import cn.itsource.service.IRoleService;
import cn.itsource.pojo.domain.Role;
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

@Tag(name = "角色表",description = "角色表")
@RestController
@RequestMapping("/manager/role")
public class RoleController{

    @Autowired
    public IRoleService roleService;

    @Operation( summary= "保存Role",description = "基础对象保存接口")
    @Parameter(name = "role",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid Role role){
        return R.success(roleService.save(role));
    }

    @Operation( summary= "修改Role",description = "基础对象修改接口")
    @Parameter(name = "role",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid Role role){
        return R.success(roleService.updateById(role));
    }

    //删除对象
    @Operation( summary= "删除Role",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(roleService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取Role",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<Role> get(@PathVariable("id")Long id){
        return R.success(roleService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询Role列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<Role>> list(@RequestBody PageQueryWrapper<Role> query){
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(roleService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询Role分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<Role>> page(@RequestBody PageQueryWrapper<Role> query){
        //分页查询
        Page<Role> page = new Page<Role>(query.getPage(),query.getRows());
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = roleService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<Role>(page.getTotal(),page.getRecords()));
    }

}
