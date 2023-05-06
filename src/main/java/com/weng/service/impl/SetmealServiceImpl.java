package com.weng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.dto.SetmealDto;
import com.weng.entity.Setmeal;
import com.weng.entity.SetmealDish;
import com.weng.exception.BusinessException;
import com.weng.mapper.SetmealDishMapper;
import com.weng.mapper.SetmealMapper;
import com.weng.service.SetmealDishService;
import com.weng.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 24431
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2023-04-27 15:10:58
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService
{
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public void addWithSetmealDish(SetmealDto setmealDto)
    {
        setmealMapper.insert(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map(item ->
        {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public SetmealDto getByIdWithSetmealDish(Long id)
    {
        Setmeal setmeal = setmealMapper.selectById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);

        /**
         * 数据库中的setmeal_dish表中的setmeal_id和dish_id为varchar，需要将其改为bigint
         */
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(lambdaQueryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);
//        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetMealId(id);
//        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    @Override
    public void updateWithSetmealDish(SetmealDto setmealDto)
    {
        setmealMapper.updateById(setmealDto);

        //删除setmeal_dish表中的相关数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishMapper.delete(lambdaQueryWrapper);

        //新增setmeal_dish表中的相关数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map(item ->
        {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void deleteWithSetmealDish(List<Long> ids)
    {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        //0表示停售，1表示启售
        lambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        Long count = setmealMapper.selectCount(lambdaQueryWrapper);

        if (count > 0)
        {
            throw new BusinessException("不能删除正在启售的套餐!");
        }

        setmealMapper.deleteBatchIds(ids);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }

    @Override
    public void updateStatus(Integer status, List<Long> ids)
    {
        LambdaUpdateWrapper<Setmeal> setmealLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        setmealLambdaUpdateWrapper.set(Setmeal::getStatus,status);
        setmealLambdaUpdateWrapper.in(Setmeal::getId,ids);
        this.update(setmealLambdaUpdateWrapper);
    }


}




