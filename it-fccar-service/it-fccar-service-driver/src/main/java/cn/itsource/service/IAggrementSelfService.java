package cn.itsource.service;

import cn.itsource.pojo.domain.AggrementSelf;
import cn.itsource.pojo.dto.AggrementDto;
import cn.itsource.pojo.dto.UpdateDto;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ????
 * @since 2024-04-11
 */
public interface IAggrementSelfService extends IService<AggrementSelf> {
    String uploadFile(MultipartFile file) throws IOException;

    AggrementSelf addAggrement(AggrementDto aggrementDto);

    UpdateDto updateAggrement(UpdateDto updateDto);

}
