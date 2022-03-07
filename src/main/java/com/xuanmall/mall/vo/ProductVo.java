package com.xuanmall.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVo {
    //前端所需数据
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private BigDecimal price;

    private Integer status;

}
