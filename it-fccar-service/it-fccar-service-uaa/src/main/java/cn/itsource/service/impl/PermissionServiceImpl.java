package cn.itsource.service.impl;

import cn.itsource.pojo.domain.Permission;
import cn.itsource.mapper.PermissionMapper;
import cn.itsource.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author zhaodi
 * @since 2024-03-29
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
