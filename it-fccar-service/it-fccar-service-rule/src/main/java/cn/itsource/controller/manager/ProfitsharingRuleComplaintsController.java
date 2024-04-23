package cn.itsource.controller.manager;

import cn.itsource.service.IProfitsharingRuleComplaintsService;
import cn.itsource.pojo.domain.ProfitsharingRuleComplaints;
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

@Tag(name = "其他费用：税，渠道费",description = "其他费用：税，渠道费")
@RestController
@RequestMapping("/manager/profitsharingRuleComplaints")
public class ProfitsharingRuleComplaintsController{

    @Autowired
    public IProfitsharingRuleComplaintsService profitsharingRuleComplaintsService;

    @Operation( summary= "保存ProfitsharingRuleComplaints",description = "基础对象保存接口")
    @Parameter(name = "profitsharingRuleComplaints",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid ProfitsharingRuleComplaints profitsharingRuleComplaints){
        return R.success(profitsharingRuleComplaintsService.save(profitsharingRuleComplaints));
    }

    @Operation( summary= "修改ProfitsharingRuleComplaints",description = "基础对象修改接口")
    @Parameter(name = "profitsharingRuleComplaints",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid ProfitsharingRuleComplaints profitsharingRuleComplaints){
        return R.success(profitsharingRuleComplaintsService.updateById(profitsharingRuleComplaints));
    }

    //删除对象
    @Operation( summary= "删除ProfitsharingRuleComplaints",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(profitsharingRuleComplaintsService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取ProfitsharingRuleComplaints",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<ProfitsharingRuleComplaints> get(@PathVariable("id")Long id){
        return R.success(profitsharingRuleComplaintsService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询ProfitsharingRuleComplaints列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<ProfitsharingRuleComplaints>> list(@RequestBody PageQueryWrapper<ProfitsharingRuleComplaints> query){
        QueryWrapper<ProfitsharingRuleComplaints> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(profitsharingRuleComplaintsService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询ProfitsharingRuleComplaints分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<ProfitsharingRuleComplaints>> page(@RequestBody PageQueryWrapper<ProfitsharingRuleComplaints> query){
        //分页查询
        Page<ProfitsharingRuleComplaints> page = new Page<ProfitsharingRuleComplaints>(query.getPage(),query.getRows());
        QueryWrapper<ProfitsharingRuleComplaints> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = profitsharingRuleComplaintsService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<ProfitsharingRuleComplaints>(page.getTotal(),page.getRecords()));
    }

}
