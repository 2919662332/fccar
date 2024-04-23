package cn.itsource.controller.manager;

import cn.itsource.service.IProfitsharingRuleDeductCancelService;
import cn.itsource.pojo.domain.ProfitsharingRuleDeductCancel;
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

@Tag(name = "司机罚款",description = "司机罚款")
@RestController
@RequestMapping("/manager/profitsharingRuleDeductCancel")
public class ProfitsharingRuleDeductCancelController{

    @Autowired
    public IProfitsharingRuleDeductCancelService profitsharingRuleDeductCancelService;

    @Operation( summary= "保存ProfitsharingRuleDeductCancel",description = "基础对象保存接口")
    @Parameter(name = "profitsharingRuleDeductCancel",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid ProfitsharingRuleDeductCancel profitsharingRuleDeductCancel){
        return R.success(profitsharingRuleDeductCancelService.save(profitsharingRuleDeductCancel));
    }

    @Operation( summary= "修改ProfitsharingRuleDeductCancel",description = "基础对象修改接口")
    @Parameter(name = "profitsharingRuleDeductCancel",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid ProfitsharingRuleDeductCancel profitsharingRuleDeductCancel){
        return R.success(profitsharingRuleDeductCancelService.updateById(profitsharingRuleDeductCancel));
    }

    //删除对象
    @Operation( summary= "删除ProfitsharingRuleDeductCancel",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(profitsharingRuleDeductCancelService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取ProfitsharingRuleDeductCancel",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<ProfitsharingRuleDeductCancel> get(@PathVariable("id")Long id){
        return R.success(profitsharingRuleDeductCancelService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询ProfitsharingRuleDeductCancel列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<ProfitsharingRuleDeductCancel>> list(@RequestBody PageQueryWrapper<ProfitsharingRuleDeductCancel> query){
        QueryWrapper<ProfitsharingRuleDeductCancel> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(profitsharingRuleDeductCancelService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询ProfitsharingRuleDeductCancel分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<ProfitsharingRuleDeductCancel>> page(@RequestBody PageQueryWrapper<ProfitsharingRuleDeductCancel> query){
        //分页查询
        Page<ProfitsharingRuleDeductCancel> page = new Page<ProfitsharingRuleDeductCancel>(query.getPage(),query.getRows());
        QueryWrapper<ProfitsharingRuleDeductCancel> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = profitsharingRuleDeductCancelService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<ProfitsharingRuleDeductCancel>(page.getTotal(),page.getRecords()));
    }

}
