package cn.itsource.pojo.query;

import lombok.Data;

//分页查询对象
@Data
public class PageQueryWrapper<T>{
    //查询参数
    private T query;
    //搜索关键字
    private String keyword;
    //当前页码
    private Integer page = 1;
    //每页条数
    private Integer rows = 10;
    //排序字段
    private String sortField;
    //排序类型
    private String sortType;
}
