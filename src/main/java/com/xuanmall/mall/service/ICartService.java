package com.xuanmall.mall.service;

import com.xuanmall.mall.form.CartAddForm;
import com.xuanmall.mall.form.CartUpdateForm;
import com.xuanmall.mall.pojo.Cart;
import com.xuanmall.mall.vo.CartVo;
import com.xuanmall.mall.vo.ResponseVo;

import java.util.List;

public interface ICartService {
    ResponseVo<CartVo> add(Integer uid,CartAddForm form);
    ResponseVo<CartVo> list(Integer uid);
    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);
    ResponseVo<CartVo> delete(Integer uid,Integer productId);
    ResponseVo<CartVo> selectAll(Integer uid);
    ResponseVo<CartVo> unSelectAll(Integer uid);
    ResponseVo<Integer> sum(Integer uid);
    List<Cart> listForCart(Integer uid);
}
