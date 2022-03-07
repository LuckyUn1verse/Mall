package com.xuanmall.mall.listener;

import com.google.gson.Gson;
import com.xuanmall.mall.pojo.PayInfo;
import com.xuanmall.mall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMsgListener {
    //rabbitMQ
    @Autowired
    private IOrderService orderService;
    @RabbitHandler
    public void process(String msg){
        log.info("接收到消息：{}",msg);
        //PayInfo最好应该pay项目提供jar包，mall项目引用jar包
        PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
        if(payInfo.getPlatformStatus().equals("SUCCESS")){
            //修改订单里的状态
            orderService.paid(payInfo.getOrderNo());
        }
    }
}
