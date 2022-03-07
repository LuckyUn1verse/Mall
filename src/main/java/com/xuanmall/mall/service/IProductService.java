package com.xuanmall.mall.service;

import com.github.pagehelper.PageInfo;
import com.xuanmall.mall.vo.ProductDetailVo;
import com.xuanmall.mall.vo.ResponseVo;

public interface IProductService {

    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    ResponseVo<ProductDetailVo> detail(Integer productId);
}
