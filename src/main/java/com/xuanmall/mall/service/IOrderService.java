package com.xuanmall.mall.service;

import com.github.pagehelper.PageInfo;
import com.xuanmall.mall.vo.OrderVo;
import com.xuanmall.mall.vo.ResponseVo;

public interface IOrderService {
    ResponseVo<OrderVo> create(Integer uid,Integer shippingId);
    ResponseVo<PageInfo> list(Integer uid,Integer pageNum,Integer pageSize);
    ResponseVo<OrderVo> detail(Integer uid,Long orderNo);//只能查自己订单
    ResponseVo cancel(Integer uid,Long orderNo);
    void paid(Long orderNo);//rabbitMQ
}
