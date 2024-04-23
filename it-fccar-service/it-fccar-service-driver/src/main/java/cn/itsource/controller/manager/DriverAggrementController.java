package cn.itsource.controller.manager;

import cn.itsource.service.IDriverAggrementService;
import cn.itsource.pojo.domain.DriverAggrement;
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

@Tag(name = "司机的协议签署",description = "司机的协议签署")
@RestController
@RequestMapping("/manager/driverAggrement")
public class DriverAggrementController{

    @Resource
    public IDriverAggrementService driverAggrementService;

    @Operation( summary= "保存DriverAggrement",description = "基础对象保存接口")
    @Parameter(name = "driverAggrement",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid DriverAggrement driverAggrement){
        return R.success(driverAggrementService.save(driverAggrement));
    }

    @Operation( summary= "修改DriverAggrement",description = "基础对象修改接口")
    @Parameter(name = "driverAggrement",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid DriverAggrement driverAggrement){
        return R.success(driverAggrementService.updateById(driverAggrement));
    }

    //删除对象
    @Operation( summary= "删除DriverAggrement",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(driverAggrementService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取DriverAggrement",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<DriverAggrement> get(@PathVariable("id")Long id){
        return R.success(driverAggrementService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询DriverAggrement列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<DriverAggrement>> list(@RequestBody PageQueryWrapper<DriverAggrement> query){
        QueryWrapper<DriverAggrement> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(driverAggrementService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询DriverAggrement分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<DriverAggrement>> page(@RequestBody PageQueryWrapper<DriverAggrement> query){
        //分页查询
        Page<DriverAggrement> page = new Page<>(query.getPage(), query.getRows());
        QueryWrapper<DriverAggrement> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = driverAggrementService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }

}
