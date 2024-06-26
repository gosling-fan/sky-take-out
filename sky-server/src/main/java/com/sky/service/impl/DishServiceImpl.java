package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 新增菜品和口味
     * @param dishDTO
     * @return
     */
    @Override
    public void addDishandFavors(DishDTO dishDTO) {
        //1.插入一条菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.add(dish);

        //2.插入多条口味信息
        //顾客输入了口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //将口味的菜品id设置好，前端得不到这条数据
        for (DishFlavor flavor:flavors) {
            flavor.setDishId(dish.getId());
        }
        if(flavors!=null&&flavors.size()>0){
            dishFlavorMapper.insert(flavors);
        }
    }
}
