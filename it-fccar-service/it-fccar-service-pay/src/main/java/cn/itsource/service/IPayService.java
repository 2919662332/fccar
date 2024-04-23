package cn.itsource.service;

import cn.itsource.dto.WxPayParams;

public interface IPayService {
    WxPayParams wxPay(String orderNo);
}
