package cn.itsource.remote.api;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.itsource.remote.pojo.SaveLoginName;
import cn.itsource.remote.pojo.SaveLoginParam;
import cn.itsource.remote.sentiel.FeignSentiel;
import cn.itsource.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "it-fccar-service-uaa",fallbackFactory = FeignSentiel.class)
public interface FeignApi {

    @PostMapping("/api/login")
    R saveLogin(@RequestBody @Valid SaveLoginParam save);

    @PostMapping("/api/saveLoginName")
    R saveLoginName(@RequestBody @Valid SaveLoginName saveLoginName);
}
