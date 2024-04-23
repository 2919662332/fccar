package cn.itsource.controller.manager;

import cn.itsource.service.IWxpayFlowService;
import cn.itsource.pojo.domain.WxpayFlow;
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

@Tag(name = "",description = "")
@RestController
@RequestMapping("/manager/wxpayFlow")
public class WxpayFlowController{

    @Autowired
    public IWxpayFlowService wxpayFlowService;

    @Operation( summary= "保存WxpayFlow",description = "基础对象保存接口")
    @Parameter(name = "wxpayFlow",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid WxpayFlow wxpayFlow){
        return R.success(wxpayFlowService.save(wxpayFlow));
    }

    @Operation( summary= "修改WxpayFlow",description = "基础对象修改接口")
    @Parameter(name = "wxpayFlow",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid WxpayFlow wxpayFlow){
        return R.success(wxpayFlowService.updateById(wxpayFlow));
    }

    //删除对象
    @Operation( summary= "删除WxpayFlow",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(wxpayFlowService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取WxpayFlow",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<WxpayFlow> get(@PathVariable("id")Long id){
        return R.success(wxpayFlowService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询WxpayFlow列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<WxpayFlow>> list(@RequestBody PageQueryWrapper<WxpayFlow> query){
        QueryWrapper<WxpayFlow> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(wxpayFlowService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询WxpayFlow分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<WxpayFlow>> page(@RequestBody PageQueryWrapper<WxpayFlow> query){
        //分页查询
        Page<WxpayFlow> page = new Page<WxpayFlow>(query.getPage(),query.getRows());
        QueryWrapper<WxpayFlow> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = wxpayFlowService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<WxpayFlow>(page.getTotal(),page.getRecords()));
    }

}
