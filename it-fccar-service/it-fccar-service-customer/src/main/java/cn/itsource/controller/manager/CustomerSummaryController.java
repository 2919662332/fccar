package cn.itsource.controller.manager;

import cn.itsource.service.ICustomerSummaryService;
import cn.itsource.pojo.domain.CustomerSummary;
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

@Tag(name = "乘客数据汇总",description = "乘客数据汇总")
@RestController
@RequestMapping("/manager/customerSummary")
public class CustomerSummaryController{

    @Autowired
    public ICustomerSummaryService customerSummaryService;

    @Operation( summary= "保存CustomerSummary",description = "基础对象保存接口")
    @Parameter(name = "customerSummary",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid CustomerSummary customerSummary){
        return R.success(customerSummaryService.save(customerSummary));
    }

    @Operation( summary= "修改CustomerSummary",description = "基础对象修改接口")
    @Parameter(name = "customerSummary",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid CustomerSummary customerSummary){
        return R.success(customerSummaryService.updateById(customerSummary));
    }

    //删除对象
    @Operation( summary= "删除CustomerSummary",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(customerSummaryService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取CustomerSummary",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<CustomerSummary> get(@PathVariable("id")Long id){
        return R.success(customerSummaryService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询CustomerSummary列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<CustomerSummary>> list(@RequestBody PageQueryWrapper<CustomerSummary> query){
        QueryWrapper<CustomerSummary> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(customerSummaryService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询CustomerSummary分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<CustomerSummary>> page(@RequestBody PageQueryWrapper<CustomerSummary> query){
        //分页查询
        Page<CustomerSummary> page = new Page<CustomerSummary>(query.getPage(),query.getRows());
        QueryWrapper<CustomerSummary> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = customerSummaryService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<CustomerSummary>(page.getTotal(),page.getRecords()));
    }

}
