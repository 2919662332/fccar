package cn.itsource.controller.remoteapi;

import cn.itsource.bo.OrderBillBo;
import cn.itsource.bo.OrderProfitBo;
import cn.itsource.exception.GlobalException;
import cn.itsource.pojo.domain.*;
import cn.itsource.remote.api.RuleFeignApi;
import cn.itsource.remote.pojo.dto.ParamsDto;
import cn.itsource.remote.pojo.dto.PricingParametersDto;
import cn.itsource.remote.pojo.dto.SetOrderParam;
import cn.itsource.result.R;
import cn.itsource.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class CalculatePriceImpl implements RuleFeignApi {

    @Resource
    private IChargeRuleStartService ruleStartService;
    @Resource
    private IChargeRuleReturnService ruleReturnService;
    @Resource
    private IChargeRuleWaitService ruleWaitService;

    @Resource
    private IProfitsharingRuleBaseService sharedRuleBaseService;

    @Resource
    private IProfitsharingRuleComplaintsService policyComplaintsService;

    @Resource
    private IProfitsharingRuleDeductCancelService ruleDeductCancelService;
    /**
     * 计算订单金额的实现类
     * @param parametersDto
     * @return
     */

    //计算订单价格
    @Override
    public R<OrderBillBo> calculatePrice(PricingParametersDto parametersDto) {


        //查询计价规则
        ChargeRuleStart ruleStart = ruleStartService.getOne(new LambdaQueryWrapper<ChargeRuleStart>()
                .le(ChargeRuleStart::getHourStart, parametersDto.getOrderDate())
                .ge(ChargeRuleStart::getHourEnd, parametersDto.getOrderDate())
        );
        //计算起步金额，如果起步里程小于基础里程，直接使用起步价格
        BigDecimal startingAmount = ruleStart.getAmount();
        BigDecimal realAmount = ruleStart.getAmount();

        //如果起步里程大于数据库的里程数就计算额外价格
        if(parametersDto.getExpectsMileage().compareTo(ruleStart.getBaseMileage())>0 && parametersDto.getExpectsMileage() != null){
            startingAmount = (parametersDto.getExpectsMileage().subtract(ruleStart.getBaseMileage())).multiply(ruleStart.getExceedEveryKmAmount()).add(ruleStart.getAmount()).setScale(2, RoundingMode.HALF_UP);
        }

        //计算实际订单价格
        if(parametersDto.getRealMileage().compareTo(ruleStart.getBaseMileage())>0 && parametersDto.getRealMileage() != null){
            realAmount = (parametersDto.getRealMileage().subtract(ruleStart.getBaseMileage())).multiply(ruleStart.getExceedEveryKmAmount()).add(ruleStart.getAmount()).setScale(2, RoundingMode.HALF_UP);
        }

        //计算返程金额
        ChargeRuleReturn ruleReturn = ruleReturnService.getOne(new LambdaQueryWrapper<ChargeRuleReturn>().eq(ChargeRuleReturn::getId, 1));

        //返程价格
        BigDecimal amountReturnTrip;
        //如果返程里程大于数据库的里程才计算价格
        if(parametersDto.getExpectsMileage().compareTo(ruleReturn.getFreeBaseReturnMileage())>0){
            amountReturnTrip = (parametersDto.getExpectsMileage().subtract(ruleReturn.getFreeBaseReturnMileage())).multiply(ruleReturn.getExceedEveryKmAmount()).setScale(2, RoundingMode.HALF_UP);
        }else{
            amountReturnTrip = BigDecimal.ZERO;
        }
        //计算总金额
        BigDecimal totalAmount = realAmount.add(amountReturnTrip);

        OrderBillBo orderBillBo = calculatePrices(realAmount,totalAmount, ruleStart, ruleReturn, amountReturnTrip);
        //返回结果
        return R.success(orderBillBo);
    }

    /**
     * 计算等时费
     * @param waitTime
     * @return
     */
    @Override
    public SetOrderParam calWaitPrice(Long waitTime) {
        if (waitTime == null){
            throw new GlobalException("非法参数waitTime");
        }
        //获取等时费计价规则
        ChargeRuleWait chargeRuleWait = ruleWaitService.list().get(0);
        SetOrderParam setOrderParam = new SetOrderParam();
        //设置等时费默认为0
        BigDecimal waitPrice = BigDecimal.ZERO;
        //只有当等待时间大于免费等待时间我才计算等时费
        if (waitTime.intValue() > chargeRuleWait.getFreeBaseWaitingMinute()){
            long l = waitTime.intValue() - chargeRuleWait.getFreeBaseWaitingMinute();
            waitPrice = new BigDecimal(l).multiply(chargeRuleWait.getExceedEveryMinuteAmount());
        }
        setOrderParam.setWaitPrice(waitPrice);
        setOrderParam.setWaitingMinute(waitTime.intValue());
        setOrderParam.setFreeBaseWaitingMinute(chargeRuleWait.getFreeBaseWaitingMinute());
        setOrderParam.setExceedEveryMinuteAmount(chargeRuleWait.getExceedEveryMinuteAmount());
        return setOrderParam;
    }
    /**
     * 计算分账
     * @param paramsDto
     * @return
     */
    @Override
    public R<OrderProfitBo> calSplitting(ParamsDto paramsDto) {
        //取消订单数判断
        ProfitsharingRuleDeductCancel cancelServiceOne = ruleDeductCancelService
                .getOne(new LambdaQueryWrapper<ProfitsharingRuleDeductCancel>()
                .le(ProfitsharingRuleDeductCancel::getNumberFrom, paramsDto.getTodayCancel())
                .ge(ProfitsharingRuleDeductCancel::getNumberTo, paramsDto.getTodayCancel()));

        //被投诉次数判断
        ProfitsharingRuleComplaints complaintsServiceOne = policyComplaintsService
                .getOne(new LambdaQueryWrapper<ProfitsharingRuleComplaints>()
                .le(ProfitsharingRuleComplaints::getStart, paramsDto.getTodayComplaint())
                .ge(ProfitsharingRuleComplaints::getEnd, paramsDto.getTodayComplaint()));

        //如果没有罚款就返回原来的金额
        if (complaintsServiceOne == null && cancelServiceOne == null){
            OrderProfitBo orderProfitBo = new OrderProfitBo();
            BigDecimal bigDecimal = new BigDecimal("0.00");
            orderProfitBo.setDriverIncome(paramsDto.getRealOrderAmount());
            orderProfitBo.setPlatformIncome(bigDecimal);
            orderProfitBo.setPlatformRate(bigDecimal);
            orderProfitBo.setDriverRate(bigDecimal);
            return R.success(orderProfitBo);
        }
        //司机的基础接单奖励
        // TODO  未写

        OrderProfitBo orderProfitBo = new OrderProfitBo();
        //司机被扣除的比例
        BigDecimal decimal = cancelServiceOne.getDriverRatioDeduct().add(complaintsServiceOne.getProportion());
        //用1减去司机被扣除的比例，得到司机因拿到手的比例
        BigDecimal count = BigDecimal.ONE.subtract(decimal);
        //用司机的订单价格乘以拿到手的比例得到真实得到的钱
        BigDecimal driverIncomes = paramsDto.getRealOrderAmount().multiply(count);
        //用最开始没被扣除比例的钱减去扣除比例的钱，得到平台到手的钱
        BigDecimal platformIncome = paramsDto.getRealOrderAmount().subtract(driverIncomes);
        orderProfitBo.setDriverIncome(driverIncomes);
        orderProfitBo.setPlatformIncome(platformIncome);
        //平台得到的比例
        orderProfitBo.setPlatformRate(decimal);
        //司机得到的比例
        orderProfitBo.setDriverRate(count);
        return R.success(orderProfitBo);
    }

    private OrderBillBo calculatePrices(BigDecimal realAmount,BigDecimal totalAmount,ChargeRuleStart ruleStart,ChargeRuleReturn ruleReturn,BigDecimal amountReturnTrip){

        ChargeRuleWait ruleWait = ruleWaitService.getOne(new LambdaQueryWrapper<ChargeRuleWait>()
                .eq(ChargeRuleWait::getId, 1)
                .eq(ChargeRuleWait::getEnable, 0));

        OrderBillBo orderBillBo = new OrderBillBo();
        //总价格
        orderBillBo.setRealOrderAmount(new BigDecimal("0.00"));
        //实付款
        orderBillBo.setRealPayAmount(realAmount);
        //基础里程（公里）
        orderBillBo.setBaseMileage(ruleStart.getBaseMileage());
        //基础里程价格
        orderBillBo.setBaseMileageAmount(ruleStart.getAmount());
        //超出基础里程的价格
        orderBillBo.setExceedBaseMileageAmount(ruleStart.getExceedEveryKmAmount());
        //里程费
        orderBillBo.setMileageAmount(totalAmount);
        //免费等待乘客分钟数
        orderBillBo.setFreeBaseWaitingMinute(ruleWait.getFreeBaseWaitingMinute());
        //返程费免费公里数
        orderBillBo.setFreeBaseReturnMileage(ruleReturn.getFreeBaseReturnMileage());
        //超出基础里程部分每公里收取的费用
        orderBillBo.setExceedBaseReturnEveryKmAmount(ruleReturn.getExceedEveryKmAmount());
        //等时超出基础免费每分钟价格
        orderBillBo.setExceedEveryMinuteAmount(ruleWait.getExceedEveryMinuteAmount());
        //返程费
        orderBillBo.setReturnAmont(amountReturnTrip);
        //其他费用
        orderBillBo.setOtherAmount(new BigDecimal("0.00"));
        //停车费
        orderBillBo.setParkingAmount(new BigDecimal("0.00"));
        return orderBillBo;
    }
}
