package cn.itsource.remote.sentiel;

import cn.itsource.bo.DriverInfoBo;
import cn.itsource.remote.api.DriverFeignApi;
import cn.itsource.result.R;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DriverFallBackSentail implements FallbackFactory<DriverFeignApi> {
    @Override
    public DriverFeignApi create(Throwable cause) {
        return new DriverFeignApi() {
            @Override
            public R<DriverInfoBo> getDriverInfo(String driverId) {
                return R.error("降级了");
            }
        };
    }
}
