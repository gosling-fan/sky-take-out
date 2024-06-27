package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void addDishandFavors(DishDTO dishDTO);

    PageResult queryPage(DishPageQueryDTO dishPageQueryDTO);

    void deleteDishBatch(List<Long> ids);

    DishVO queryDishByIdWithFlavor(Long id);

    void updateDishWithFlavor(DishDTO dishDTO);
}
