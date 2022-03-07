package com.xuanmall.mall;

import com.xuanmall.mall.consts.MallConst;
import com.xuanmall.mall.exception.UserLoginException;
import com.xuanmall.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//拦截器类,选择接口重写
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    /*true表示继续流程，false表示中断*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle...");

        User user=(User)request.getSession().getAttribute(MallConst.CURRENT_USER);
        if(user==null){
           log.info("user=null");
           throw new UserLoginException();
//           return false;
        }
        return true;
    }
}
