package cn.itsource.remote.api;

import cn.itsource.bo.OrderBillBo;
import cn.itsource.bo.OrderProfitBo;
import cn.itsource.remote.pojo.dto.ParamsDto;
import cn.itsource.remote.pojo.dto.PricingParametersDto;
import cn.itsource.remote.pojo.dto.SetOrderParam;
import cn.itsource.remote.sential.RuleFallBack;
import cn.itsource.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

@FeignClient(value = "it-fccar-service-rule",fallbackFactory = RuleFallBack.class)
public interface RuleFeignApi {
    @PostMapping("/order/calculatePrice")
    R<OrderBillBo> calculatePrice(@RequestBody @Valid PricingParametersDto parametersDto);

    /**
     * 传递一个分钟用于计算等时费
     * @param waitTime
     * @return
     */

    @PostMapping("/order/calWaitPrice/{waitTime}")
    SetOrderParam calWaitPrice(@PathVariable Long waitTime);

    /**
     * 计算分账
     * @param paramsDto
     * @return
     */
    @PostMapping("/order/calSplitting")
    R<OrderProfitBo> calSplitting(@RequestBody @Valid ParamsDto paramsDto);
}
