package com.xuanmall.mall.controller;

import com.xuanmall.mall.consts.MallConst;
import com.xuanmall.mall.form.CartAddForm;
import com.xuanmall.mall.form.CartUpdateForm;
import com.xuanmall.mall.pojo.User;
import com.xuanmall.mall.service.ICartService;
import com.xuanmall.mall.vo.CartVo;
import com.xuanmall.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class CartController {
    @Autowired
    private ICartService cartService;

    @GetMapping("/carts")//购物车列表
    public ResponseVo<CartVo> list(HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return cartService.list(user.getId());
    }

    @PostMapping("/carts")//添加商品
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm cartAddForm,
                                  HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return cartService.add(user.getId(),cartAddForm);
    }

    @PutMapping("/carts/{productId}")//更新购物车
    public ResponseVo<CartVo> update(@PathVariable Integer productId,
                                     @Valid @RequestBody CartUpdateForm form,
                                     HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return cartService.update(user.getId(),productId,form);
    }

    @DeleteMapping("/carts/{productId}")//删除购物车商品
    public ResponseVo<CartVo> delete(@PathVariable Integer productId,
                                     HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return cartService.delete(user.getId(),productId);
    }

    @PutMapping("/carts/selectAll")//购物车商品全选
    public ResponseVo<CartVo> selectAll(HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return cartService.selectAll(user.getId());
    }

    @PutMapping("/carts/unSelectAll")//购物车商品全不选
    public ResponseVo<CartVo> unSelectAll(HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return cartService.unSelectAll(user.getId());
    }

    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return cartService.sum(user.getId());
    }
}
