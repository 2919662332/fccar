package cn.itsource.controller.manager;

import cn.itsource.service.IDriverAuthMaterialService;
import cn.itsource.pojo.domain.DriverAuthMaterial;
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

@Tag(name = "司机实名资料",description = "司机实名资料")
@RestController
@RequestMapping("/manager/driverAuthMaterial")
public class DriverAuthMaterialController{

    @Autowired
    public IDriverAuthMaterialService driverAuthMaterialService;

    @Operation( summary= "保存DriverAuthMaterial",description = "基础对象保存接口")
    @Parameter(name = "driverAuthMaterial",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid DriverAuthMaterial driverAuthMaterial){
        return R.success(driverAuthMaterialService.save(driverAuthMaterial));
    }

    @Operation( summary= "修改DriverAuthMaterial",description = "基础对象修改接口")
    @Parameter(name = "driverAuthMaterial",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid DriverAuthMaterial driverAuthMaterial){
        return R.success(driverAuthMaterialService.updateById(driverAuthMaterial));
    }

    //删除对象
    @Operation( summary= "删除DriverAuthMaterial",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(driverAuthMaterialService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取DriverAuthMaterial",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<DriverAuthMaterial> get(@PathVariable("id")Long id){
        return R.success(driverAuthMaterialService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询DriverAuthMaterial列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<DriverAuthMaterial>> list(@RequestBody PageQueryWrapper<DriverAuthMaterial> query){
        QueryWrapper<DriverAuthMaterial> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(driverAuthMaterialService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询DriverAuthMaterial分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<DriverAuthMaterial>> page(@RequestBody PageQueryWrapper<DriverAuthMaterial> query){
        //分页查询
        Page<DriverAuthMaterial> page = new Page<DriverAuthMaterial>(query.getPage(),query.getRows());
        QueryWrapper<DriverAuthMaterial> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = driverAuthMaterialService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<DriverAuthMaterial>(page.getTotal(),page.getRecords()));
    }

}
