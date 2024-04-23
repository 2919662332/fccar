package cn.itsource.controller.manager;

import cn.itsource.service.IDriverService;
import cn.itsource.pojo.domain.Driver;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.annotation.Resource;
import javax.validation.Valid;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.itsource.pojo.query.PageQueryWrapper;
import cn.itsource.result.R;
import java.util.List;
import cn.itsource.result.PageResult;

@Tag(name = "司机对象",description = "司机对象")
@RestController
@RequestMapping("/manager/driver")
public class DriverController{

    @Resource
    public IDriverService driverService;

    @Operation( summary= "保存Driver",description = "基础对象保存接口")
    @Parameter(name = "driver",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid Driver driver){
        return R.success(driverService.save(driver));
    }

    @Operation( summary= "修改Driver",description = "基础对象修改接口")
    @Parameter(name = "driver",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid Driver driver){
        return R.success(driverService.updateById(driver));
    }

    //删除对象
    @Operation( summary= "删除Driver",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(driverService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取Driver",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<Driver> get(@PathVariable("id")Long id){
        return R.success(driverService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询Driver列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<Driver>> list(@RequestBody PageQueryWrapper<Driver> query){
        QueryWrapper<Driver> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(driverService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询Driver分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<Driver>> page(@RequestBody PageQueryWrapper<Driver> query){
        //分页查询
        Page<Driver> page = new Page<>(query.getPage(),query.getRows());
        QueryWrapper<Driver> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = driverService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<>(page.getTotal(),page.getRecords()));
    }

}
