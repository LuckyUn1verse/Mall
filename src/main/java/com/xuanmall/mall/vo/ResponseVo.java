package com.xuanmall.mall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xuanmall.mall.enums.ResponseEnum;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
//避免返回NULL数据
public class ResponseVo<T> {
    private Integer status;
    private String msg;
    private T data;//范型

    private ResponseVo(Integer status, String msg) {//返回页面的数据
        this.status = status;
        this.msg = msg;
    }
    private ResponseVo(Integer status, T data) {//返回页面的数据
        this.status = status;
        this.data = data;
    }
    public static <T>ResponseVo<T> successByMsg(String msg){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),msg);
    }
    public static <T>ResponseVo<T> success(T data){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),data);
    }
    
    public static <T>ResponseVo<T> success(){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getDesc());
    }
    public static <T>ResponseVo<T> error(ResponseEnum responseEnum){
        return new ResponseVo<>(responseEnum.getCode(),responseEnum.getDesc());
    }
    public static <T>ResponseVo<T> error(ResponseEnum responseEnum,String msg){
        return new ResponseVo<>(responseEnum.getCode(),msg);
    }
    public static <T>ResponseVo<T> error(ResponseEnum responseEnum, BindingResult bindingResult){
        return new ResponseVo<>(responseEnum.getCode(),
                Objects.requireNonNull(bindingResult.getFieldError()).getField()+" "+
                bindingResult.getFieldError().getDefaultMessage());
    }
}
