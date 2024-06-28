package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增菜品和口味
     * @param dishDTO
     * @return
     */
    @Override
    @Transactional
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
    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult queryPage(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page =dishMapper.query(dishPageQueryDTO);
        long total = page.getTotal();
        List<DishVO> result = page.getResult();
        return new PageResult(total,result);
    }
    @Transactional
    @Override
    public void deleteDishBatch(List<Long> ids) {
        //1.在起售中的菜品不可以删除
        List<Dish> dishList = dishMapper.queryByIds(ids);
        for (Dish dish:dishList) {
            //如果存在正在起售的菜品，抛出异常
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw  new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //2.在套餐中的菜品不可以删除
        List<SetmealDish> setmealDishList = setmealDishMapper.queryByDishIds(ids);
        if(setmealDishList.size()>0){
            throw  new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //3.批量删除菜品
        dishMapper.deleteBatch(ids);
        //4.批量删除菜品口味
        dishFlavorMapper.deleteBatch(ids);
    }

    /**
     * 根据id查询菜品和口味
     * @param id
     * @return
     */
    @Override
    public DishVO queryDishByIdWithFlavor(Long id) {

        //1.根据id查询口味信息
        Dish dish =dishMapper.queryById(id);
        //2.根据dish_id查询口味信息

         List<DishFlavor> dishFlavorList =dishFlavorMapper.queryByDishId(dish.getId());
        //3.封装为DishVO对象中
        DishVO dishVO = new DishVO();
        if(dish!=null)
            BeanUtils.copyProperties(dish,dishVO);
        if(dishFlavorList!=null&&dishFlavorList.size()>0)
            BeanUtils.copyProperties(dishFlavorList,dishVO);
        return dishVO;
    }

    @Transactional
    @Override
    public void updateDishWithFlavor(DishDTO dishDTO) {
        //1.更新dish菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //2.更新菜品口味信息
            //1.删除原来信息
        List<DishFlavor> dishFlavorList = dishFlavorMapper.queryByDishId(dish.getId());
        //如果口味信息不为空
        if(dishFlavorList!=null&&dishFlavorList.size()>0){
            dishFlavorMapper.deleteByDishId(dish.getId());
        }
        //得到前端传入数据，再设置好dish_id信息
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        for (DishFlavor dishflavor:dishFlavors) {
            dishflavor.setDishId(dish.getId());
        }
        //2.插入新口味信息
        dishFlavorMapper.insert(dishFlavors);
    }
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }
    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.queryByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

}
