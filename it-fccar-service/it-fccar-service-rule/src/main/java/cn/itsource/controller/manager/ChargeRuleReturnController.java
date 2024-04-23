package cn.itsource.controller.manager;

import cn.itsource.service.IChargeRuleReturnService;
import cn.itsource.pojo.domain.ChargeRuleReturn;
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

@Tag(name = "返程费计价规则",description = "返程费计价规则")
@RestController
@RequestMapping("/manager/chargeRuleReturn")
public class ChargeRuleReturnController{

    @Autowired
    public IChargeRuleReturnService chargeRuleReturnService;

    @Operation( summary= "保存ChargeRuleReturn",description = "基础对象保存接口")
    @Parameter(name = "chargeRuleReturn",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid ChargeRuleReturn chargeRuleReturn){
        return R.success(chargeRuleReturnService.save(chargeRuleReturn));
    }

    @Operation( summary= "修改ChargeRuleReturn",description = "基础对象修改接口")
    @Parameter(name = "chargeRuleReturn",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid ChargeRuleReturn chargeRuleReturn){
        return R.success(chargeRuleReturnService.updateById(chargeRuleReturn));
    }

    //删除对象
    @Operation( summary= "删除ChargeRuleReturn",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(chargeRuleReturnService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取ChargeRuleReturn",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<ChargeRuleReturn> get(@PathVariable("id")Long id){
        return R.success(chargeRuleReturnService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询ChargeRuleReturn列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<ChargeRuleReturn>> list(@RequestBody PageQueryWrapper<ChargeRuleReturn> query){
        QueryWrapper<ChargeRuleReturn> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(chargeRuleReturnService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询ChargeRuleReturn分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<ChargeRuleReturn>> page(@RequestBody PageQueryWrapper<ChargeRuleReturn> query){
        //分页查询
        Page<ChargeRuleReturn> page = new Page<ChargeRuleReturn>(query.getPage(),query.getRows());
        QueryWrapper<ChargeRuleReturn> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = chargeRuleReturnService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<ChargeRuleReturn>(page.getTotal(),page.getRecords()));
    }

}
