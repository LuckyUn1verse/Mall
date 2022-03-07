package com.xuanmall.mall.service;

import com.github.pagehelper.PageInfo;
import com.xuanmall.mall.form.ShippingForm;
import com.xuanmall.mall.vo.ResponseVo;

import java.util.Map;

public interface IShippingService {
        ResponseVo<Map<String,Integer>> add(Integer uid, ShippingForm form);//添加地址

        ResponseVo delete(Integer uid,Integer shippingId);//删除地址

        ResponseVo update(Integer uid,Integer shippingId,ShippingForm form);//更新地址

        ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);//地址分页
    }

