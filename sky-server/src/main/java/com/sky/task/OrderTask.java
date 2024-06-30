package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理一直未付款的订单
     */
    @Scheduled(cron = "0 * * * * *")
    public void payTask(){
        log.info("处理一直未付款的订单");
        //1.查询未付款的订单
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList =orderService.queryOrder(Orders.UN_PAID,time);
        if(ordersList!=null&&ordersList.size()>0){
            for (Orders orders:ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，已取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.updatePayStatus(orders);
            }
        }

    }

    /**
     * 处理一直处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * *") //每天一点执行一次
    public void DeliveryOrder(){
        log.info("处理一直处于派送中的订单");
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.queryByStatusTime(Orders.DELIVERY_IN_PROGRESS,time);
        if(ordersList!=null&&ordersList.size()>0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.updatePayStatus(orders);
            }
        }
    }
}
