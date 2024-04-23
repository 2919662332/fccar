package cn.itsource.controller.manager;

import cn.itsource.service.IDriverMaterialAuthLogService;
import cn.itsource.pojo.domain.DriverMaterialAuthLog;
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

@Tag(name = "材料审核日志",description = "材料审核日志")
@RestController
@RequestMapping("/manager/driverMaterialAuthLog")
public class DriverMaterialAuthLogController{

    @Autowired
    public IDriverMaterialAuthLogService driverMaterialAuthLogService;

    @Operation( summary= "保存DriverMaterialAuthLog",description = "基础对象保存接口")
    @Parameter(name = "driverMaterialAuthLog",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid DriverMaterialAuthLog driverMaterialAuthLog){
        return R.success(driverMaterialAuthLogService.save(driverMaterialAuthLog));
    }

    @Operation( summary= "修改DriverMaterialAuthLog",description = "基础对象修改接口")
    @Parameter(name = "driverMaterialAuthLog",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid DriverMaterialAuthLog driverMaterialAuthLog){
        return R.success(driverMaterialAuthLogService.updateById(driverMaterialAuthLog));
    }

    //删除对象
    @Operation( summary= "删除DriverMaterialAuthLog",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(driverMaterialAuthLogService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取DriverMaterialAuthLog",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<DriverMaterialAuthLog> get(@PathVariable("id")Long id){
        return R.success(driverMaterialAuthLogService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询DriverMaterialAuthLog列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<DriverMaterialAuthLog>> list(@RequestBody PageQueryWrapper<DriverMaterialAuthLog> query){
        QueryWrapper<DriverMaterialAuthLog> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(driverMaterialAuthLogService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询DriverMaterialAuthLog分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<DriverMaterialAuthLog>> page(@RequestBody PageQueryWrapper<DriverMaterialAuthLog> query){
        //分页查询
        Page<DriverMaterialAuthLog> page = new Page<DriverMaterialAuthLog>(query.getPage(),query.getRows());
        QueryWrapper<DriverMaterialAuthLog> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = driverMaterialAuthLogService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<DriverMaterialAuthLog>(page.getTotal(),page.getRecords()));
    }

}
