package cn.itsource.service.impl;

import cn.itsource.pojo.domain.LoginRole;
import cn.itsource.mapper.LoginRoleMapper;
import cn.itsource.service.ILoginRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色中间表 服务实现类
 * </p>
 *
 * @author zhaodi
 * @since 2024-03-29
 */
@Service
public class LoginRoleServiceImpl extends ServiceImpl<LoginRoleMapper, LoginRole> implements ILoginRoleService {

}
