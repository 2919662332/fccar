package cn.itsource.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.itsource.exception.GlobalException;
import cn.itsource.mapper.DriverMapper;
import cn.itsource.pojo.domain.AggrementSelf;
import cn.itsource.mapper.AggrementSelfMapper;
import cn.itsource.pojo.domain.Driver;
import cn.itsource.pojo.dto.AggrementDto;
import cn.itsource.pojo.dto.UpdateDto;
import cn.itsource.service.IAggrementSelfService;
import cn.itsource.template.OssTemplate;
import cn.itsource.utils.BitStatesUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ????
 * @since 2024-04-11
 */
@Service
public class AggrementSelfServiceImpl extends ServiceImpl<AggrementSelfMapper, AggrementSelf> implements IAggrementSelfService {
    @Resource
    private OssTemplate ossTemplate;
    @Resource
    private DriverMapper driverMapper;
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String aggrementFile = ossTemplate.uploadAggrementFile(file);
        if (aggrementFile != null){
            return aggrementFile;
        }else {
            throw new GlobalException("文件获取上传失败！");
        }
    }

    @Override
    @Transactional
    public AggrementSelf  addAggrement(AggrementDto aggrementDto) {
        long id = IdUtil.getSnowflake(1,5).nextId();
        AggrementSelf aggrementSelf = new AggrementSelf();
        aggrementSelf.setId(id);
        BeanUtils.copyProperties(aggrementDto,aggrementSelf);
        aggrementSelf.setPhone("10010");
        aggrementSelf.setIdCardNum("511725200101235718");
        aggrementSelf.setCreateTime(new Date());
        aggrementSelf.setDriverId(1778327851771760640L);
        super.save(aggrementSelf);
        AggrementSelf byId = super.getById(id);
        Driver driver = driverMapper.selectById(byId.getDriverId());
        driver.setBitState(BitStatesUtil.addState(driver.getBitState(),BitStatesUtil.OP_DRIVER_AGREEMENT));
        driverMapper.updateById(driver);
        return aggrementSelf;
    }

    @Override
    @Transactional
    public UpdateDto updateAggrement(UpdateDto updateDto) {
        UpdateDto dbUpdateDto = new UpdateDto();
        AggrementSelf byId = super.getById(updateDto.getId());
        dbUpdateDto.setStatus(byId.getStatus());
        dbUpdateDto.setId(byId.getId());
        byId.setStatus("2");
        Driver driver = driverMapper.selectById(byId.getDriverId());
        driver.setBitState(BitStatesUtil.removeState(driver.getBitState(), BitStatesUtil.OP_DRIVER_AGREEMENT));
        driverMapper.updateById(driver);
        super.updateById(byId);
        return dbUpdateDto;
    }
}
