package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     * @return
     */
    @Select("select * from orders where number =#{orderNumber}")
    Orders queryByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    @Update("update orders set pay_status=#{payStatus} where number =#{number}")
    void update(Orders orders);
    @Select("select * from orders where status=#{status} and  order_time <#{time}")
    List<Orders> query(Integer status, LocalDateTime time);
    @Select("select * from orders where status=#{status} and  order_time <#{dateTime}")
    List<Orders> queryByStatusTime(Integer status, LocalDateTime dateTime);

    void updatePayStatus(Orders orders);
}
