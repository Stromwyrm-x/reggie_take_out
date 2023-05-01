package com.weng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.dto.DishDto;
import com.weng.entity.Dish;
import com.weng.entity.DishFlavor;
import com.weng.mapper.DishFlavorMapper;
import com.weng.service.DishFlavorService;
import com.weng.service.DishService;
import com.weng.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 24431
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2023-04-27 15:10:58
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService
{

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    public void addWithFlavor(DishDto dishDto)
    {
        dishMapper.insert(dishDto);
        //dishFlavor中没有dishId,insert以后会自动生成dishId
        for (DishFlavor flavor : dishDto.getFlavors())
        {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    public DishDto getByIdWithFlavor(Long id)
    {
        Dish dish = dishMapper.selectById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(lambdaQueryWrapper);
        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto)
    {
        dishMapper.updateById(dishDto);
        //更新dish_flavor表信息delete操作
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorMapper.delete(lambdaQueryWrapper);

        //更新dish_flavor表信息insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors)
        {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(flavors);
    }
}




