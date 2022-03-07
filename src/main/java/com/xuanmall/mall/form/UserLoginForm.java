package com.xuanmall.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginForm {
    @NotBlank
    // 用于String判断空格
    //@NotEmpty 用于集合
    //@NotNull
    private String username;
    @NotBlank
    private String password;

}
