package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品和口味
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品和口味")

    public Result addDish(@RequestBody DishDTO dishDTO){
        dishService.addDishandFavors(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult = dishService.queryPage(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据ids批量删除菜品
     * @param ids
     * @return
     */
    @ApiOperation("根据id批量删除菜品")
    @DeleteMapping
    public Result  deleteDishBatch(@RequestParam List<Long> ids){ //将前端的多个id,封装到list结合中
        dishService.deleteDishBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品和口味
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> queryById(@PathVariable Long id){
        DishVO dishVO =dishService.queryDishByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 更新菜品信息
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("更新菜品信息")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDishWithFlavor(dishDTO);
        return Result.success();
    }
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

}
