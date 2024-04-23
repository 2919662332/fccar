package cn.itsource.controller.manager;

import cn.itsource.service.IProfitsharingRuleBaseService;
import cn.itsource.pojo.domain.ProfitsharingRuleBase;
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

@Tag(name = "平台抽成基础比例",description = "平台抽成基础比例")
@RestController
@RequestMapping("/manager/profitsharingRuleBase")
public class ProfitsharingRuleBaseController{

    @Autowired
    public IProfitsharingRuleBaseService profitsharingRuleBaseService;

    @Operation( summary= "保存ProfitsharingRuleBase",description = "基础对象保存接口")
    @Parameter(name = "profitsharingRuleBase",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid ProfitsharingRuleBase profitsharingRuleBase){
        return R.success(profitsharingRuleBaseService.save(profitsharingRuleBase));
    }

    @Operation( summary= "修改ProfitsharingRuleBase",description = "基础对象修改接口")
    @Parameter(name = "profitsharingRuleBase",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid ProfitsharingRuleBase profitsharingRuleBase){
        return R.success(profitsharingRuleBaseService.updateById(profitsharingRuleBase));
    }

    //删除对象
    @Operation( summary= "删除ProfitsharingRuleBase",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(profitsharingRuleBaseService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取ProfitsharingRuleBase",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<ProfitsharingRuleBase> get(@PathVariable("id")Long id){
        return R.success(profitsharingRuleBaseService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询ProfitsharingRuleBase列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<ProfitsharingRuleBase>> list(@RequestBody PageQueryWrapper<ProfitsharingRuleBase> query){
        QueryWrapper<ProfitsharingRuleBase> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(profitsharingRuleBaseService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询ProfitsharingRuleBase分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<ProfitsharingRuleBase>> page(@RequestBody PageQueryWrapper<ProfitsharingRuleBase> query){
        //分页查询
        Page<ProfitsharingRuleBase> page = new Page<ProfitsharingRuleBase>(query.getPage(),query.getRows());
        QueryWrapper<ProfitsharingRuleBase> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = profitsharingRuleBaseService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<ProfitsharingRuleBase>(page.getTotal(),page.getRecords()));
    }

}
