package com.xuanmall.mall.service.impl;

import com.xuanmall.mall.dao.UserMapper;
import com.xuanmall.mall.enums.ResponseEnum;
import com.xuanmall.mall.enums.RoleEnum;
import com.xuanmall.mall.pojo.User;
import com.xuanmall.mall.service.IUserService;
import com.xuanmall.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public ResponseVo<User> register(User user) {


        int countByUsername = userMapper.countByUsername(user.getUsername());
        if(countByUsername>0){
//            throw new RuntimeException("该username已注册");
            return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
        }

        int countByEmail = userMapper.countByEmail(user.getEmail());
        if(countByEmail>0){
//            throw new RuntimeException("该email已使用");
        return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
        }
        user.setRole(RoleEnum.CUSTOMER.getCode());
//        MD5加密  摘要算法
        user.setPassword( DigestUtils.md5DigestAsHex(
                user.getPassword().getBytes(StandardCharsets.UTF_8)));

        int resultCount=userMapper.insertSelective(user);
        if(resultCount==0){
//            throw new RuntimeException("注册失败");
        return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<User> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if(user==null){
            //用户不存在(返回用户名或密码错误)
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        if(!user.getPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex( password.getBytes(StandardCharsets.UTF_8)))){
            //密码错误(返回用户名或密码错误)
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        user.setPassword("");//设置返回页面的信息无密码
        return ResponseVo.success(user);
    }
}
