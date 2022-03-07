package com.xuanmall.mall.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
//    0-管理员 1-普通用户
    ADMIN(0),
    CUSTOMER(1),
    ;
    Integer code;

    RoleEnum(Integer code) {
        this.code = code;
    }
}
