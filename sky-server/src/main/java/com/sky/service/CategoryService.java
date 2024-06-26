package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult pageToQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void addCategory(CategoryDTO categoryDTO);

    List<Category> queryByType(Integer type);

    void deleteCategoryById(Long id);

    void updateCategory(CategoryDTO categoryDTO);


    void enableOrDisable(Integer status, Long id);
}
