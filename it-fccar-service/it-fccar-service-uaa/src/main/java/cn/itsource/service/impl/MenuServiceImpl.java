package cn.itsource.service.impl;

import cn.itsource.pojo.domain.Menu;
import cn.itsource.mapper.MenuMapper;
import cn.itsource.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zhaodi
 * @since 2024-03-29
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
