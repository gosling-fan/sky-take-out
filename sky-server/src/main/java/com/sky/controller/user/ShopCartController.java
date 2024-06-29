package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/shoppingCart")
@Api(tags = "购物车管理")
public class ShopCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加菜品或者套餐到购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加菜品或者套餐到购物车")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 展示用户购物车列表
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("展示用户购物车列表")
    public Result<List<ShoppingCart>> list(){
       List<ShoppingCart> shoppingCartList= shoppingCartService.list();
       return Result.success(shoppingCartList);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result cleanShoppingCart(){
        shoppingCartService.clean();
        return Result.success();
    }

    /**
     * 删除购物车中的一条数据
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    @ApiOperation("删除购物车中的一条数据")
    public Result deleteShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.deleteShoppingCart(shoppingCartDTO);
        return Result.success();
    }

}
