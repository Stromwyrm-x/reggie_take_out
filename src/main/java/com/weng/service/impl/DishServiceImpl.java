package com.weng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.dto.DishDto;
import com.weng.entity.Dish;
import com.weng.entity.DishFlavor;
import com.weng.entity.Setmeal;
import com.weng.entity.SetmealDish;
import com.weng.exception.BusinessException;
import com.weng.mapper.DishFlavorMapper;
import com.weng.mapper.SetmealDishMapper;
import com.weng.mapper.SetmealMapper;
import com.weng.service.DishFlavorService;
import com.weng.service.DishService;
import com.weng.mapper.DishMapper;
import com.weng.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

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

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
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
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorMapper.delete(lambdaQueryWrapper);

        //更新dish_flavor表信息insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        for (DishFlavor flavor : flavors)
        {
            flavor.setDishId(dishDto.getId());
        }

       /* Object collect = flavors.stream().map(new Function<DishFlavor, Object>()
        {
            @Override
            public Object apply(DishFlavor dishFlavor)
            {
                dishFlavor.setDishId(dishDto.getId());
                return dishFlavor;
            }
        }).collect();*/
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 删除菜品的同时，还要删除菜品口味表
     * @param ids
     */
    @Override
    public void deleteWithFlavor(List<Long> ids)
    {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Dish::getId, ids);
        //只查询启售的菜品
        dishLambdaQueryWrapper.eq(Dish::getStatus, 1);
        Long count = dishMapper.selectCount(dishLambdaQueryWrapper);
        if (count > 0)
        {
            throw new BusinessException("不能删除正在启售的菜品!");
        }

        dishMapper.deleteBatchIds(ids);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorMapper.delete(dishFlavorLambdaQueryWrapper);
    }

    /**
     * 停售、启售菜品，本质上是改变dish的status
     * @param status
     * @param ids
     */
    @Override
    public void updateStatus(Integer status, List<Long> ids)
    {
        //若要停售，则判断这个dish是否在正在启售的套餐内，如果在套餐内，则不能停售
        //select count(*) from setmeal_dish where dish_id in #{ids}
        if(status==0)
        {
            LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper=new LambdaQueryWrapper<>();
            setmealDishLambdaQueryWrapper.in(SetmealDish::getDishId,ids);
            Long count = setmealDishMapper.selectCount(setmealDishLambdaQueryWrapper);
            if (count>0)
            {
                throw new BusinessException("不能停售正处于套餐里的菜品");
            }
        }

        //update dish set status=#{status} where id in #{ids};
        LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        dishLambdaUpdateWrapper.in(Dish::getId,ids);
        dishLambdaUpdateWrapper.set(Dish::getStatus,status);
        this.update(dishLambdaUpdateWrapper);


    }


}




