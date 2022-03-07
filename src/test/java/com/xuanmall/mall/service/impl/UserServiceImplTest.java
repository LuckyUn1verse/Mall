package com.xuanmall.mall.service.impl;

import com.xuanmall.mall.MallApplicationTests;
import com.xuanmall.mall.enums.ResponseEnum;
import com.xuanmall.mall.enums.RoleEnum;
import com.xuanmall.mall.pojo.User;
import com.xuanmall.mall.service.IUserService;
import com.xuanmall.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
//用于单测不会写入数据库
public class UserServiceImplTest extends MallApplicationTests {
public static final String USERNAME="jack2";
public static final String PASSWORD="123456";
    @Autowired
    private IUserService userService;
    @Before
    public void register() {
        User user=new User(USERNAME,PASSWORD,"jack2@qq.com", RoleEnum.CUSTOMER.getCode());
        userService.register(user);
    }
    @Test
    public void login(){
        ResponseVo<User> responseVo=userService.login(USERNAME,PASSWORD);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());

    }
}