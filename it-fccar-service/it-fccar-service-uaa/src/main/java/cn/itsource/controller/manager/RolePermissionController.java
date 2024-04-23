package cn.itsource.controller.manager;

import cn.itsource.service.IRolePermissionService;
import cn.itsource.pojo.domain.RolePermission;
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

@Tag(name = "角色权限表",description = "角色权限表")
@RestController
@RequestMapping("/manager/rolePermission")
public class RolePermissionController{

    @Autowired
    public IRolePermissionService rolePermissionService;

    @Operation( summary= "保存RolePermission",description = "基础对象保存接口")
    @Parameter(name = "rolePermission",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid RolePermission rolePermission){
        return R.success(rolePermissionService.save(rolePermission));
    }

    @Operation( summary= "修改RolePermission",description = "基础对象修改接口")
    @Parameter(name = "rolePermission",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid RolePermission rolePermission){
        return R.success(rolePermissionService.updateById(rolePermission));
    }

    //删除对象
    @Operation( summary= "删除RolePermission",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(rolePermissionService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取RolePermission",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<RolePermission> get(@PathVariable("id")Long id){
        return R.success(rolePermissionService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询RolePermission列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<RolePermission>> list(@RequestBody PageQueryWrapper<RolePermission> query){
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(rolePermissionService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询RolePermission分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<RolePermission>> page(@RequestBody PageQueryWrapper<RolePermission> query){
        //分页查询
        Page<RolePermission> page = new Page<RolePermission>(query.getPage(),query.getRows());
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = rolePermissionService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<RolePermission>(page.getTotal(),page.getRecords()));
    }

}
