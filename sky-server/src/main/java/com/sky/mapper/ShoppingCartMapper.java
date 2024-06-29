package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 根据shoppingCartDTO查询购物车
     * @param shoppingCart
     */
    List<ShoppingCart> query(ShoppingCart shoppingCart);

    /**
     * 更新购物车中数据
     * @param shoppingCart
     */
    @Update("update  shopping_cart set name=#{name},image=#{image},user_id=#{userId},dish_id=#{dishId}," +
            "setmeal_id=#{setmealId},dish_flavor=#{dishFlavor},number=#{number},amount=#{amount},create_time=#{createTime" +
            "} where id =#{id}")
    void update(ShoppingCart shoppingCart);

    /**
     *
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart(name,image,user_id,dish_id,setmeal_id,dish_flavor,number,amount," +
            "create_time) value(#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor}," +
            "#{number},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);
    void delete(ShoppingCart shoppingCart);
}
