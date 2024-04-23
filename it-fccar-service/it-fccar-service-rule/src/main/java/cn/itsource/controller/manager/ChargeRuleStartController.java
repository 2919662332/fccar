package cn.itsource.controller.manager;

import cn.itsource.dto.OrderPriceParamsDto;
import cn.itsource.pojo.bo.OrderPriceBo;
import cn.itsource.service.IChargeRuleStartService;
import cn.itsource.pojo.domain.ChargeRuleStart;
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

@Tag(name = "计价规则-起步价",description = "计价规则-起步价")
@RestController
@RequestMapping("/manager/chargeRuleStart")
public class ChargeRuleStartController{

    @Autowired
    public IChargeRuleStartService chargeRuleStartService;

    @Operation( summary= "保存ChargeRuleStart",description = "基础对象保存接口")
    @Parameter(name = "chargeRuleStart",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid ChargeRuleStart chargeRuleStart){
        return R.success(chargeRuleStartService.save(chargeRuleStart));
    }

    @Operation( summary= "修改ChargeRuleStart",description = "基础对象修改接口")
    @Parameter(name = "chargeRuleStart",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid ChargeRuleStart chargeRuleStart){
        return R.success(chargeRuleStartService.updateById(chargeRuleStart));
    }

    //删除对象
    @Operation( summary= "删除ChargeRuleStart",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(chargeRuleStartService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取ChargeRuleStart",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<ChargeRuleStart> get(@PathVariable("id")Long id){
        return R.success(chargeRuleStartService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询ChargeRuleStart列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<ChargeRuleStart>> list(@RequestBody PageQueryWrapper<ChargeRuleStart> query){
        QueryWrapper<ChargeRuleStart> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(chargeRuleStartService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询ChargeRuleStart分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<ChargeRuleStart>> page(@RequestBody PageQueryWrapper<ChargeRuleStart> query){
        //分页查询
        Page<ChargeRuleStart> page = new Page<ChargeRuleStart>(query.getPage(),query.getRows());
        QueryWrapper<ChargeRuleStart> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = chargeRuleStartService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<ChargeRuleStart>(page.getTotal(),page.getRecords()));
    }

}
