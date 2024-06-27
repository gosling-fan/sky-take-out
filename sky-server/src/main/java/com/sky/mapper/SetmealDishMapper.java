package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 通过dish_id查询菜品所在的套餐
     * @param dishIds
     * @return
     */
//    select * form setmeal_dish where dish_id in (1,2,3,4)
    List<SetmealDish> queryByDishIds(List<Long> dishIds);
}
