package cn.itsource.mapper;

import cn.itsource.pojo.domain.Login;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

/**
 * <p>
 * 登录表 Mapper 接口
 * </p>
 *
 * @author zhaodi
 * @since 2024-03-29
 */
public interface LoginMapper extends BaseMapper<Login> {


    List<String> selectPermissionByLoginId(Long id);

}
