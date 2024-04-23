package cn.itsource.remote.api;

import cn.itsource.bo.DriverInfoBo;
import cn.itsource.remote.sentiel.DriverFallBackSentail;
import cn.itsource.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "it-fccar-service-driver",fallbackFactory = DriverFallBackSentail.class)
public interface DriverFeignApi {

    @PostMapping("/api/GetdriverInfo/{driverId}")
    R<DriverInfoBo> getDriverInfo(@PathVariable String driverId);
}
