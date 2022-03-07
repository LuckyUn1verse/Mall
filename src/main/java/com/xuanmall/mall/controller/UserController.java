package com.xuanmall.mall.controller;

import com.xuanmall.mall.consts.MallConst;
import com.xuanmall.mall.form.UserLoginForm;
import com.xuanmall.mall.form.UserRegisterForm;
import com.xuanmall.mall.pojo.User;
import com.xuanmall.mall.service.IUserService;
import com.xuanmall.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private IUserService userService;
    @PostMapping("/user/register")
    public ResponseVo register(@Valid/*表单校验*/
                               @RequestBody UserRegisterForm userForm){
//        @RequestParam(value="username")
//        JSON用@RequestBody
//        log.info("username={}",userForm.getUsername());
////        return ResponseVo.success();
//        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        User user=new User();
        BeanUtils.copyProperties(userForm,user);
//        对象拷贝方法
        return userService.register(user);
    }
    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                    HttpSession session
                                    /*http设置Session*/){
        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());
//      设置  Session
        session.setAttribute(MallConst.CURRENT_USER,userResponseVo.getData());
        return userResponseVo;
    }
    //session保存在内存里
    //Cookie跨域就会失效，localhost与127.0.0.1也是跨域
    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session){
        log.info("/user sessionId={}",session.getId());
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);

//        其实应该再查一遍
        return ResponseVo.success(user);
    }
    //TODO 判断登录状态
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session){
        log.info("/user/logout sessionId={}",session.getId());

            session.removeAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success();
    }
}
