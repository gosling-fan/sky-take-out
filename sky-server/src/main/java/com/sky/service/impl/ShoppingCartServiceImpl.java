package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //1.判断购物车中是否有同类菜品或者套餐
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.query(shoppingCart);
        //2.如果有，则将number字段加1,
        if(shoppingCartList!=null&&shoppingCartList.size()>0){
            //如果有数据，应该只有一条数据
            ShoppingCart shoppingCart1 = shoppingCartList.get(0);
            shoppingCart1.setNumber(shoppingCart1.getNumber()+1);
            shoppingCartMapper.update(shoppingCart1);
        }else {
            //3.没有，重新插入一条数据到购物车表
            //1.若加入的是菜品
            if(shoppingCartDTO.getDishId()!=null){
                Dish dish = dishMapper.queryById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else{
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());

            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCartMapper.insert(shoppingCart);
        }


    }
    /**
     * 展示用户购物车列表
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.query(shoppingCart);
        return shoppingCartList;
    }

    @Override
    public void clean() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCartMapper.delete(shoppingCart);
    }

    @Override
    public void deleteShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCartMapper.delete(shoppingCart);
    }
}
