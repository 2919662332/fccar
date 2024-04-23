package cn.itsource.remote.sential;

import cn.itsource.bo.OrderBillBo;
import cn.itsource.bo.OrderProfitBo;
import cn.itsource.remote.api.RuleFeignApi;
import cn.itsource.remote.pojo.dto.ParamsDto;
import cn.itsource.remote.pojo.dto.PricingParametersDto;
import cn.itsource.remote.pojo.dto.SetOrderParam;
import cn.itsource.result.R;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class RuleFallBack implements FallbackFactory<RuleFeignApi> {


    @Override
    public RuleFeignApi create(Throwable cause) {
        return new RuleFeignApi() {
            @Override
            public R<OrderBillBo> calculatePrice(PricingParametersDto parametersDto) {
                return R.error("降级了");
            }

            @Override
            public SetOrderParam calWaitPrice(Long waitTime) {
                return null;
            }

            @Override
            public R<OrderProfitBo> calSplitting(ParamsDto paramsDto) {
                return R.error("降级了");
            }
        };
    }
}
