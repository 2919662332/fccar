package cn.itsource.controller.remoteapi;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.itsource.pojo.domain.Login;
import cn.itsource.remote.api.FeignApi;
import cn.itsource.remote.pojo.SaveLoginName;
import cn.itsource.remote.pojo.SaveLoginParam;
import cn.itsource.result.R;
import cn.itsource.service.ILoginService;
import cn.itsource.utils.NameUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class SaveLoginImpl implements FeignApi {
    @Resource
    private ILoginService service;

    /**
     * 保存登陆信息
     * @param save
     * @return
     */
    @Override
    @SaIgnore
    public R saveLogin(SaveLoginParam save) {
        Login login = new Login();
        BeanUtils.copyProperties(save,login);
        login.setAvatar("https://tupian.qqw21.com/article/UploadPic/2020-5/20205262373861268.jpg");
        login.setNickName(NameUtil.getName());
        login.setName(NameUtil.getName());
        login.setCreateTime(new Date());
        service.save(login);
        return R.success("保存成功！");
    }

    @Override
    public R saveLoginName(SaveLoginName saveLoginName) {
        if (saveLoginName.getId() != null && saveLoginName.getName() != null){
            Login loginInfo = service.getById(saveLoginName.getId());
            loginInfo.setName(saveLoginName.getName());
            loginInfo.setUpdateTime(new Date());
            service.updateById(loginInfo);
            return R.success("保存成功！");
        }
        return R.error("保存登录信息错误！");
    }
}
