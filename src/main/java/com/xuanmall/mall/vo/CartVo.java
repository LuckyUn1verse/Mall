package com.xuanmall.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/*购物车*/
@Data
public class CartVo {
    private List<CartProductVo> cartProductVoList;

    private Boolean selectAll;//购物车全选键

    private BigDecimal cartTotalPrice;

    private Integer cartTotalQuantity;

}
