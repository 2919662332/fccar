package cn.itsource.controller.manager;

import cn.itsource.service.IDriverSummaryService;
import cn.itsource.pojo.domain.DriverSummary;
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

@Tag(name = "司机结算对象",description = "司机结算对象")
@RestController
@RequestMapping("/manager/driverSummary")
public class DriverSummaryController{

    @Autowired
    public IDriverSummaryService driverSummaryService;

    @Operation( summary= "保存DriverSummary",description = "基础对象保存接口")
    @Parameter(name = "driverSummary",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid DriverSummary driverSummary){
        return R.success(driverSummaryService.save(driverSummary));
    }

    @Operation( summary= "修改DriverSummary",description = "基础对象修改接口")
    @Parameter(name = "driverSummary",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid DriverSummary driverSummary){
        return R.success(driverSummaryService.updateById(driverSummary));
    }

    //删除对象
    @Operation( summary= "删除DriverSummary",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(driverSummaryService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取DriverSummary",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<DriverSummary> get(@PathVariable("id")Long id){
        return R.success(driverSummaryService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询DriverSummary列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<DriverSummary>> list(@RequestBody PageQueryWrapper<DriverSummary> query){
        QueryWrapper<DriverSummary> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(driverSummaryService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询DriverSummary分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<DriverSummary>> page(@RequestBody PageQueryWrapper<DriverSummary> query){
        //分页查询
        Page<DriverSummary> page = new Page<DriverSummary>(query.getPage(),query.getRows());
        QueryWrapper<DriverSummary> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = driverSummaryService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<DriverSummary>(page.getTotal(),page.getRecords()));
    }

}
