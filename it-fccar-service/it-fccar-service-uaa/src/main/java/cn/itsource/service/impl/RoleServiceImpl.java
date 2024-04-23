package cn.itsource.service.impl;

import cn.itsource.pojo.domain.Role;
import cn.itsource.mapper.RoleMapper;
import cn.itsource.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zhaodi
 * @since 2024-03-29
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
