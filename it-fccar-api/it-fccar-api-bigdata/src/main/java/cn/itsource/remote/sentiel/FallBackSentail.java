package cn.itsource.remote.sentiel;

import cn.itsource.dto.DriverPoint;
import cn.itsource.remote.api.BigFeignApi;
import cn.itsource.result.R;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FallBackSentail implements FallbackFactory<BigFeignApi> {
    @Override
    public BigFeignApi create(Throwable cause) {
        return new BigFeignApi() {
            @Override
            public R<Boolean> savePoint(DriverPoint point) {
                return R.error("Cannot save point");
            }

            @Override
            public R<List<DriverPoint>> selectPoint(String orderNo) {
                return R.error("Cannot select point");
            }
        };
    }
}
