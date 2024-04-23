package cn.itsource.controller.manager;

import cn.itsource.pojo.dto.AggrementDto;
import cn.itsource.pojo.dto.UpdateDto;
import cn.itsource.service.IAggrementSelfService;
import cn.itsource.pojo.domain.AggrementSelf;
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

import java.io.IOException;
import java.util.List;
import cn.itsource.result.PageResult;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "",description = "")
@RestController
@RequestMapping("/manager/aggrementSelf")
public class AggrementSelfController{

    @Autowired
    public IAggrementSelfService aggrementSelfService;

    @Operation( summary= "保存AggrementSelf",description = "基础对象保存接口")
    @Parameter(name = "aggrementSelf",description = "保存的对象",required = true)
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid AggrementSelf aggrementSelf){
        return R.success(aggrementSelfService.save(aggrementSelf));
    }

    @Operation( summary= "修改AggrementSelf",description = "基础对象修改接口")
    @Parameter(name = "aggrementSelf",description = "修改的对象",required = true)
    @PutMapping
    public R<Boolean> update(@RequestBody  @Valid AggrementSelf aggrementSelf){
        return R.success(aggrementSelfService.updateById(aggrementSelf));
    }

    //删除对象
    @Operation( summary= "删除AggrementSelf",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id){
        return R.success(aggrementSelfService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取AggrementSelf",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<AggrementSelf> get(@PathVariable("id")Long id){
        return R.success(aggrementSelfService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询AggrementSelf列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<AggrementSelf>> list(@RequestBody PageQueryWrapper<AggrementSelf> query){
        QueryWrapper<AggrementSelf> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(aggrementSelfService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询AggrementSelf分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<AggrementSelf>> page(@RequestBody PageQueryWrapper<AggrementSelf> query){
        //分页查询
        Page<AggrementSelf> page = new Page<AggrementSelf>(query.getPage(),query.getRows());
        QueryWrapper<AggrementSelf> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = aggrementSelfService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<AggrementSelf>(page.getTotal(),page.getRecords()));
    }

    @PostMapping("/importExcel")
    public R importExcel(MultipartFile file) throws IOException {
        return R.success(aggrementSelfService.uploadFile(file));
    }
    @PostMapping("/addAggrement")
    public R addAggrement(@RequestBody AggrementDto aggrementDto) {
        return R.success(aggrementSelfService.addAggrement(aggrementDto));
    }
    @PostMapping("/updateAggrement")
    public R updateAggrement(@RequestBody UpdateDto updateDto) {
        return R.success(aggrementSelfService.updateAggrement(updateDto));
    }
}
