package cn.itsource.controller.manager;

import cn.itsource.service.IOrderBillService;
import cn.itsource.pojo.domain.OrderBill;
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

@Tag(name = "订单账单表",description = "订单账单表")
@RestController
@RequestMapping("/manager/orderBill")
public class OrderBillController{

    @Autowired
    public IOrderBillService orderBillService;

    @Operation( summary= "保存OrderBill",description = "基础对象保存接口")
    @Parameter(name = "orderBill",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid OrderBill orderBill){
        return R.success(orderBillService.save(orderBill));
    }

    @Operation( summary= "修改OrderBill",description = "基础对象修改接口")
    @Parameter(name = "orderBill",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid OrderBill orderBill){
        return R.success(orderBillService.updateById(orderBill));
    }

    //删除对象
    @Operation( summary= "删除OrderBill",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(orderBillService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取OrderBill",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<OrderBill> get(@PathVariable("id")Long id){
        return R.success(orderBillService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询OrderBill列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<OrderBill>> list(@RequestBody PageQueryWrapper<OrderBill> query){
        QueryWrapper<OrderBill> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(orderBillService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询OrderBill分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<OrderBill>> page(@RequestBody PageQueryWrapper<OrderBill> query){
        //分页查询
        Page<OrderBill> page = new Page<OrderBill>(query.getPage(),query.getRows());
        QueryWrapper<OrderBill> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = orderBillService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<OrderBill>(page.getTotal(),page.getRecords()));
    }

}
