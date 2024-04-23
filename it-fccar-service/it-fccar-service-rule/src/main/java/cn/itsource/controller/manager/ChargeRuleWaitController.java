package cn.itsource.controller.manager;

import cn.itsource.domian.dto.WaitPriceParamsDto;
import cn.itsource.pojo.bo.WaitPriceBo;
import cn.itsource.service.IChargeRuleWaitService;
import cn.itsource.pojo.domain.ChargeRuleWait;
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

@Tag(name = "等待费用",description = "等待费用")
@RestController
@RequestMapping("/manager/chargeRuleWait")
public class ChargeRuleWaitController{

    @Autowired
    public IChargeRuleWaitService chargeRuleWaitService;

    @Operation( summary= "保存ChargeRuleWait",description = "基础对象保存接口")
    @Parameter(name = "chargeRuleWait",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid ChargeRuleWait chargeRuleWait){
        return R.success(chargeRuleWaitService.save(chargeRuleWait));
    }

    @Operation( summary= "修改ChargeRuleWait",description = "基础对象修改接口")
    @Parameter(name = "chargeRuleWait",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid ChargeRuleWait chargeRuleWait){
        return R.success(chargeRuleWaitService.updateById(chargeRuleWait));
    }

    //删除对象
    @Operation( summary= "删除ChargeRuleWait",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(chargeRuleWaitService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取ChargeRuleWait",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<ChargeRuleWait> get(@PathVariable("id")Long id){
        return R.success(chargeRuleWaitService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询ChargeRuleWait列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<ChargeRuleWait>> list(@RequestBody PageQueryWrapper<ChargeRuleWait> query){
        QueryWrapper<ChargeRuleWait> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(chargeRuleWaitService.list(wrapper));
    }

    /**
     * @param query 分页查询
     * @return {@link R}<{@link PageResult}<{@link ChargeRuleWait}>>
     *///分页查询
    @Operation( summary= "查询ChargeRuleWait分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<ChargeRuleWait>> page(@RequestBody PageQueryWrapper<ChargeRuleWait> query){
        //分页查询
        Page<ChargeRuleWait> page = new Page<ChargeRuleWait>(query.getPage(),query.getRows());
        QueryWrapper<ChargeRuleWait> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = chargeRuleWaitService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<ChargeRuleWait>(page.getTotal(),page.getRecords()));
    }

}
