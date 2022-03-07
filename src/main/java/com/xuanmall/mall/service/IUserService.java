package com.xuanmall.mall.service;

import com.xuanmall.mall.pojo.User;
import com.xuanmall.mall.vo.ResponseVo;

public interface IUserService {
//   注册
    ResponseVo register(User user);

//   登录
    ResponseVo<User> login(String username,String password);
//
}
