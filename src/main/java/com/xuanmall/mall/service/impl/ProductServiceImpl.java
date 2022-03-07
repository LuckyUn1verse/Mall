package com.xuanmall.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xuanmall.mall.dao.ProductMapper;
import com.xuanmall.mall.enums.ResponseEnum;
import com.xuanmall.mall.pojo.Product;
import com.xuanmall.mall.service.ICategoryService;
import com.xuanmall.mall.service.IProductService;
import com.xuanmall.mall.vo.ProductDetailVo;
import com.xuanmall.mall.vo.ProductVo;
import com.xuanmall.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xuanmall.mall.enums.ProductStatusEnum.DELETE;
import static com.xuanmall.mall.enums.ProductStatusEnum.OFF_SALE;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        Set<Integer> categoryIdSet=new HashSet<>();
        if(categoryId!=null) {
            categoryService.findSubCategoryId(categoryId, categoryIdSet);
            categoryIdSet.add(categoryId);//子类加自身
        }
        PageHelper.startPage(pageNum,pageSize);//分页助手
        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        List<ProductVo> productVoList = productList.stream()
                .map(e -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e, productVo);
                    return productVo;
                })
                .collect(Collectors.toList());
//        log.info("products={}",products);
        PageInfo pageInfo=new PageInfo<>(productList);
        pageInfo.setList(productVoList);

        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        //只对确定的条件判断
        if(product.getStatus().equals(OFF_SALE.getCode())||product.getStatus().equals(DELETE.getCode())){
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_OR_DELETE);
        }
        ProductDetailVo productDetailVo=new ProductDetailVo();
        BeanUtils.copyProperties(product,productDetailVo);
        productDetailVo.setStock(product.getStock() > 100 ? 100 : product.getStock());
        return ResponseVo.success(productDetailVo);
    }
}
