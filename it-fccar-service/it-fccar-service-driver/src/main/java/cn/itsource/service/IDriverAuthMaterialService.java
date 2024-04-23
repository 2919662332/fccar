package cn.itsource.service;

import cn.itsource.pojo.domain.DriverAuthMaterial;
import cn.itsource.pojo.dto.MaterialDto;
import cn.itsource.result.R;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 司机实名资料 服务类
 * </p>
 *
 * @author ????
 * @since 2024-03-25
 */
public interface IDriverAuthMaterialService extends IService<DriverAuthMaterial> {

    R<DriverAuthMaterial> submitAuthMaterials(DriverAuthMaterial authMaterial);

    R<DriverAuthMaterial> queryEchoData();

    R auditMaterial(MaterialDto materialDto);

    R cancelAuditMaterial(MaterialDto materialDto);

}
