import cn.itsource.BigDataApplication;
import cn.itsource.dto.DriverPoint;
import cn.itsource.service.BigDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = BigDataApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class test001 {
    @Resource
    private BigDataService bigDataService;

    @Test
    public void test002(){
        DriverPoint driverPoint1 = new DriverPoint(1L,"30.562135","103.992686",1779345328752431104L,"1780182148831956992");
        DriverPoint driverPoint2 = new DriverPoint(2L,"30.566069","104.007913",1779345328752431104L,"1780182148831956992");
        DriverPoint driverPoint3 = new DriverPoint(3L,"30.57618","104.005567",1779345328752431104L,"1780182148831956992");
        DriverPoint driverPoint4 = new DriverPoint(4L,"30.573667","103.991105",1779345328752431104L,"1780182148831956992");
        DriverPoint driverPoint5 = new DriverPoint(5L,"30.581412","103.988441",1779345328752431104L,"1780182148831956992");

        bigDataService.savePoint(driverPoint1);
        bigDataService.savePoint(driverPoint2);
        bigDataService.savePoint(driverPoint3);
        bigDataService.savePoint(driverPoint4);
        bigDataService.savePoint(driverPoint5);
    }
    @Test
    public void test003(){
        List<DriverPoint> driverPoints = bigDataService.selectPoint("1780182148831956992");
        log.info("driverPoints: " + driverPoints);
    }
}
