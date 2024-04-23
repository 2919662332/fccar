package cn.itsource.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverInfoBo {
    //今日投诉次数
    private Integer todayComplaint;

    //今日取消订单数
    private Integer todayCancel;
}
