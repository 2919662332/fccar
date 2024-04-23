package cn.itsource.controller.manager;

import cn.itsource.service.IOrderProfitsharingService;
import cn.itsource.pojo.domain.OrderProfitsharing;
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

@Tag(name = "平台司机分账表",description = "平台司机分账表")
@RestController
@RequestMapping("/manager/orderProfitsharing")
public class OrderProfitsharingController{

    @Autowired
    public IOrderProfitsharingService orderProfitsharingService;

    @Operation( summary= "保存OrderProfitsharing",description = "基础对象保存接口")
    @Parameter(name = "orderProfitsharing",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid OrderProfitsharing orderProfitsharing){
        return R.success(orderProfitsharingService.save(orderProfitsharing));
    }

    @Operation( summary= "修改OrderProfitsharing",description = "基础对象修改接口")
    @Parameter(name = "orderProfitsharing",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid OrderProfitsharing orderProfitsharing){
        return R.success(orderProfitsharingService.updateById(orderProfitsharing));
    }

    //删除对象
    @Operation( summary= "删除OrderProfitsharing",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(orderProfitsharingService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取OrderProfitsharing",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<OrderProfitsharing> get(@PathVariable("id")Long id){
        return R.success(orderProfitsharingService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询OrderProfitsharing列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<OrderProfitsharing>> list(@RequestBody PageQueryWrapper<OrderProfitsharing> query){
        QueryWrapper<OrderProfitsharing> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(orderProfitsharingService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询OrderProfitsharing分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<OrderProfitsharing>> page(@RequestBody PageQueryWrapper<OrderProfitsharing> query){
        //分页查询
        Page<OrderProfitsharing> page = new Page<OrderProfitsharing>(query.getPage(),query.getRows());
        QueryWrapper<OrderProfitsharing> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = orderProfitsharingService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<OrderProfitsharing>(page.getTotal(),page.getRecords()));
    }

}
