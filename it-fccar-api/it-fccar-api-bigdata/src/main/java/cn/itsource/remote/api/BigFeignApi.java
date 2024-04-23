package cn.itsource.remote.api;

import cn.itsource.dto.DriverPoint;
import cn.itsource.remote.sentiel.FallBackSentail;
import cn.itsource.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(value = "it-fccar-service-bigdata",fallbackFactory = FallBackSentail.class)
public interface BigFeignApi {


    @PostMapping("/api/hbase/save")
    R<Boolean> savePoint(@RequestBody DriverPoint point);

    @PostMapping("/api/hbase/selectPoint/{orderNo}")
    R<List<DriverPoint>> selectPoint(@PathVariable String orderNo);
}
