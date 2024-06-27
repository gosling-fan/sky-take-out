package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 新增n条口味信息
     * @param flavors
     */
     void insert(List<DishFlavor> flavors) ;

    /**
     * 根据dishIds批量删除口味
     * @param dishIds
     */
//    delete from dish_flavor where dish_id in (1,2,3,5)
    void deleteBatch(List<Long> dishIds);

    /**
     * 根据dishId查询口味
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
   List<DishFlavor> queryByDishId(Long dishId);

    /**
     * 根据dishId删除口味
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id =#{dishId}")
    void deleteByDishId(Long dishId);
}
