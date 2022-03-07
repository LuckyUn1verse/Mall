package com.xuanmall.mall.service.impl;

import com.google.gson.Gson;
import com.xuanmall.mall.dao.ProductMapper;
import com.xuanmall.mall.enums.ProductStatusEnum;
import com.xuanmall.mall.enums.ResponseEnum;
import com.xuanmall.mall.form.CartAddForm;
import com.xuanmall.mall.form.CartUpdateForm;
import com.xuanmall.mall.pojo.Cart;
import com.xuanmall.mall.pojo.Product;
import com.xuanmall.mall.service.ICartService;
import com.xuanmall.mall.vo.CartProductVo;
import com.xuanmall.mall.vo.CartVo;
import com.xuanmall.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements ICartService {
    Integer quantity=1;//添加到购物车里的数量
    private final static String CART_REDIS_KEY_TEMPLATE="card_%d";
    private Gson gson=new Gson();
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public ResponseVo<CartVo> add(Integer uid,CartAddForm form) {
        Product product = productMapper.selectByPrimaryKey(form.getProductId());

        //判断商品是否存在
        if(product==null){
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }

        //判断商品是否正常在售
        if(!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())){
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_OR_DELETE);
        }

        //判断商品库存是否充足
        if(product.getStock()<=0){
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }

        //写入到redis
        //key:cart_1,value应该实时查询价格之类，预先存入不会变动的数据

        HashOperations<String,String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey=String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Cart cart;
        String value = opsForHash.get(redisKey, String.valueOf(product.getId()));
        if(StringUtils.isEmpty(value)){
            //没有该商品,新增
            cart = new Cart(product.getId(), quantity, form.getSelected());
        }else {
            //有则数量加一
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity()+quantity);
        }
        opsForHash.put(String.format(CART_REDIS_KEY_TEMPLATE,uid),
                String.valueOf(product.getId()),
                gson.toJson(cart));//把Cart对象转成String

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> list(Integer uid) {//购物车列表
        HashOperations<String,String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey=String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Map<String, String> entries = opsForHash.entries(redisKey);

        boolean selectAll=true;
        Integer cartTotalQuantity=0;
        BigDecimal cartTotalPrice=BigDecimal.ZERO;

        CartVo cartVo=new CartVo();
        List<CartProductVo> cartProductVoList=new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);
            //TODO 需要优化，使用mysql里的in
            Product product = productMapper.selectByPrimaryKey(productId);
            if(product!=null){
                CartProductVo cartProductVo=new CartProductVo(productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                        );
                cartProductVoList.add(cartProductVo);

                if(!cart.getProductSelected()){
                    selectAll=false;
                }
                //计算总价只计算选中的
                cartTotalPrice=cartTotalPrice.add(cartProductVo.getProductTotalPrice());
            }
            cartTotalQuantity+=cart.getQuantity();
        }
        //有一个没选中就不算全选
        cartVo.setSelectAll(selectAll);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {
        HashOperations<String,String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey=String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Cart cart;
        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if(StringUtils.isEmpty(value)){
            //没有该商品,报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
            //有,修改内容
        cart = gson.fromJson(value, Cart.class);
        if(form.getQuantity()!=null
                &&form.getQuantity()>=0){
            cart.setQuantity(form.getQuantity());
        }
        if(form.getSelected()!=null){
            cart.setProductSelected(form.getSelected());
        }

        opsForHash.put(redisKey,String.valueOf(productId),gson.toJson((cart)));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String,String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey=String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Cart cart;
        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if(StringUtils.isEmpty(value)){
            //没有该商品,报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }

        opsForHash.delete(redisKey,String.valueOf(productId));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        HashOperations<String,String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey=String.format(CART_REDIS_KEY_TEMPLATE,uid);


        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey,String.valueOf(cart.getProductId()),
                    gson.toJson(cart));
        }
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> unSelectAll(Integer uid) {
        HashOperations<String,String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey=String.format(CART_REDIS_KEY_TEMPLATE,uid);


        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey,String.valueOf(cart.getProductId()),
                    gson.toJson(cart));
        }
        return list(uid);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        Integer sum=listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0,Integer::sum);
        return ResponseVo.success(sum);
    }

    public List<Cart> listForCart(Integer uid){
        HashOperations<String,String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey=String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Map<String, String> entries = opsForHash.entries(redisKey);

        List<Cart> cartList=new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            cartList.add(gson.fromJson(entry.getValue(),Cart.class));
        }
        return cartList;
    }
}
