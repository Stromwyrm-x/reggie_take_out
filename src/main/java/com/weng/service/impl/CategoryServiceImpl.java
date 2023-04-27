package com.weng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.entity.Category;
import com.weng.entity.Dish;
import com.weng.entity.Setmeal;
import com.weng.mapper.CategoryMapper;
import com.weng.service.CategoryService;
import com.weng.service.DishService;
import com.weng.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weng.exception.BusinessException;

/**
 * @author 24431
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
 * @createDate 2023-04-26 22:14:34
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService
{

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 若菜品或套餐中有这个categoryId，那么不能删除
     * @param ids
     */
    @Override
    public void removeById(Long ids)
    {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);
        //select count(*) from dish where category_id={ids}
        long count_dish = dishService.count(dishLambdaQueryWrapper);
        if (count_dish > 0)
        {
            throw new BusinessException("已关联菜品，不能删除！");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        long count_setmeal = setmealService.count(setmealLambdaQueryWrapper);
        if (count_setmeal > 0)
        {
            throw new BusinessException("已关联菜品，不能删除！");
        }

        categoryMapper.deleteById(ids);
    }

}




