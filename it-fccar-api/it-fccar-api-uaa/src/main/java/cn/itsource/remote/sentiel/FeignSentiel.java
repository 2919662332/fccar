package cn.itsource.remote.sentiel;

import cn.itsource.remote.api.FeignApi;
import cn.itsource.remote.pojo.SaveLoginName;
import cn.itsource.remote.pojo.SaveLoginParam;
import cn.itsource.result.R;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignSentiel implements FallbackFactory<FeignApi> {


    @Override
    public FeignApi create(Throwable cause) {
        return new FeignApi() {
            @Override
            public R saveLogin(SaveLoginParam save) {
                cause.printStackTrace();
                return R.error("降级了");
            }

            @Override
            public R saveLoginName(SaveLoginName saveLoginName) {
                cause.printStackTrace();
                return R.error("降级了");
            }
        };
    }
}
