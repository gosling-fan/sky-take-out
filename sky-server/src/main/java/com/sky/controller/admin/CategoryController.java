package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "菜品分类管理")
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分类分页查询")
    public Result<PageResult> pageToQuery(CategoryPageQueryDTO categoryPageQueryDTO){
            log.info("前端数据：{}",categoryPageQueryDTO);
           PageResult pageResult = categoryService.pageToQuery(categoryPageQueryDTO);
           return Result.success(pageResult);
    }

    /**
     * 新增菜品分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品分类")
    public  Result addCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 根据类型查询菜品分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询菜品分类")
    public Result<List<Category>> listCategory(Integer type){
        List<Category> list =categoryService.queryByType(type);
        return Result.success(list);
    }

    /**
     * 根据id删除菜品分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除菜品分类")
    public Result deleteCategoryById(Long id){
        categoryService.deleteCategoryById(id);
        return Result.success();
    }
    @PutMapping
    @ApiOperation("修改菜品分类")
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(categoryDTO);
        return  Result.success();
    }

    /**
     * 启用或者禁用菜品分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用或禁用菜品分类")
    public Result enableOrDisable(@PathVariable Integer status,Long id){
        categoryService.enableOrDisable(status,id);
        return Result.success();
    }
 }
