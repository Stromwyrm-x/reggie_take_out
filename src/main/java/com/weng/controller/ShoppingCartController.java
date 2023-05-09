package com.weng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weng.common.Result;
import com.weng.common.util.BaseContext;
import com.weng.entity.ShoppingCart;
import com.weng.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController
{
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart)
    {
        Long user_id = BaseContext.getCurrentId();
        shoppingCart.setUserId(user_id);

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, user_id);
        if (shoppingCart.getDishId() != null)
        {
            //传的是dish
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else
        {
            //传的是setmeal
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        //传过来的数据是没有number的
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);

        if (shoppingCartServiceOne == null)
        {
            //第一次加入，新增(自己添加create_time,因为购物车中没有update_time等其它字段，所以不能用MetaObjectHandler)
            shoppingCart.setCreateTime(LocalDateTime.now());
            //虽然说数据库会自动加上number=1，但是这里的shoppingCart中的number却一直为null，所以返回给前端的number的也是null。
            //为了让前端显示出来，这里还是要setNumber一下
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCartServiceOne = shoppingCart;
        } else
        {
            //已经有数据，则number+1即可
            Integer number = shoppingCartServiceOne.getNumber();
            shoppingCartServiceOne.setNumber(number + 1);

            shoppingCartService.updateById(shoppingCartServiceOne);
        }

        return Result.success(shoppingCartServiceOne);
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list()
    {
        Long user_id = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, user_id);
        shoppingCartLambdaQueryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return Result.success(shoppingCartList);
    }


    @DeleteMapping("/clean")
    public Result<String> clean()
    {
        Long user_id = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, user_id);
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        return Result.success();
    }

    /**
     * 减号按钮，写麻了，这代码又臭又长
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public Result<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart)
    {
        Long user_id = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, user_id);

        if (shoppingCart.getDishId() != null)
        {
            //减少的是dish
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
            ShoppingCart dishShoppingCart = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);

            Integer dishShoppingCartNumber = dishShoppingCart.getNumber() - 1;
            dishShoppingCart.setNumber(dishShoppingCartNumber);

            if (dishShoppingCartNumber > 0)
            {
//                dishShoppingCart.setNumber(dishShoppingCartNumber);
                shoppingCartService.updateById(dishShoppingCart);
            } else if (dishShoppingCartNumber == 0)
            {
                shoppingCartService.removeById(dishShoppingCart.getId());
            }

            return Result.success(dishShoppingCart);
        }
        if (shoppingCart.getSetmealId() != null)
        {
            //减少的是setmeal
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            ShoppingCart setmealShoppingCart = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);

            Integer setmealShoppingCartNumber = setmealShoppingCart.getNumber() - 1;
            setmealShoppingCart.setNumber(setmealShoppingCartNumber);

            if (setmealShoppingCartNumber > 0)
            {
//                setmealShoppingCart.setNumber(setmealShoppingCartNumber);
                shoppingCartService.updateById(setmealShoppingCart);
            } else if (setmealShoppingCartNumber == 0)
            {
                shoppingCartService.removeById(setmealShoppingCart.getId());
            }
            return Result.success(setmealShoppingCart);
        }

        return Result.error("修改购物车失败!");
    }

}
