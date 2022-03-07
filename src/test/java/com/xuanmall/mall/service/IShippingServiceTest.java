package com.xuanmall.mall.service;

import com.xuanmall.mall.MallApplicationTests;
import com.xuanmall.mall.enums.ResponseEnum;
import com.xuanmall.mall.form.ShippingForm;
import com.xuanmall.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
@Slf4j
public class IShippingServiceTest extends MallApplicationTests {
    @Autowired
    private IShippingService shippingService;

    private Integer uid=1;

    private ShippingForm form;

    private Integer shippingId;
    @Before
    public void before(){
        ShippingForm form = new ShippingForm();
        form.setReceiverName("杨先生");
        form.setReceiverAddress("龙湖大道405");
        form.setReceiverCity("武汉");
        form.setReceiverMobile("13612345678");
        form.setReceiverProvince("湖北");
        form.setReceiverPhone("83434314");
        form.setReceiverDistrict("武昌区");
        form.setReceiverZip("330500");
        this.form=form;

        add();
    }

    public void add() {
        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(uid, form);
        log.info("result={}", responseVo);
        this.shippingId = responseVo.getData().get("shippingId");
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @After
    public void delete() {
        ResponseVo responseVo = shippingService.delete(uid, shippingId);
        log.info("result={}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void update() {
        form.setReceiverCity("杭州");
        ResponseVo responseVo = shippingService.update(uid, shippingId, form);
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void list() {
        ResponseVo responseVo = shippingService.list(uid,1,10);
        log.info("result={}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}