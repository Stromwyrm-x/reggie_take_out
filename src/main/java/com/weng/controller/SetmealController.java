package com.weng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weng.common.Result;
import com.weng.dto.DishDto;
import com.weng.dto.SetmealDto;
import com.weng.entity.Category;
import com.weng.entity.Dish;
import com.weng.entity.Setmeal;
import com.weng.entity.SetmealDish;
import com.weng.service.CategoryService;
import com.weng.service.DishService;
import com.weng.service.SetmealDishService;
import com.weng.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController
{
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @PostMapping
    public Result<String> add(@RequestBody SetmealDto setmealDto)
    {
        setmealService.addWithSetmealDish(setmealDto);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<Page<SetmealDto>> page(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "5") Integer pageSize,
                                         String name)
    {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, lambdaQueryWrapper);

        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        List<Setmeal> records = setmealPage.getRecords();

        List<SetmealDto> list = records.stream().map(item ->
        {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return Result.success(setmealDtoPage);
    }

    /**
     * 根据setmeal的id来查找返回对应的SetmealDto
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> get(@PathVariable Long id)
    {
        SetmealDto setmealDto = setmealService.getByIdWithSetmealDish(id);
        return Result.success(setmealDto);
    }

    /**
     * 根据setmeal的id来查找包含的dish,此dish中还要包含setmealdish
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public Result<List<DishDto>> getDish(@PathVariable Long id)
    {
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(setmealDishLambdaQueryWrapper);

        List<DishDto> dishDtos = setmealDishList.stream().map(item ->
        {
            DishDto dishDto = new DishDto();
//            BeanUtils.copyProperties(item, dishDto);
            Dish dish = dishService.getById(item.getDishId());
            BeanUtils.copyProperties(dish,dishDto);
            //dishDto中包含了copies,应该就是用来记录一个setmeal里对应的一种dish总共有几份
            dishDto.setCopies(item.getCopies());
            return dishDto;
        }).collect(Collectors.toList());

        return Result.success(dishDtos);
    }


    @GetMapping("/list")
    public Result<List<Setmeal>> listByCategoryId(Setmeal setmeal)
    {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        setmealLambdaQueryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(setmealLambdaQueryWrapper);


        return Result.success(setmealList);
    }

    @PutMapping
    public Result<String> update(@RequestBody SetmealDto setmealDto)
    {
        setmealService.updateWithSetmealDish(setmealDto);
        return Result.success();
    }

    /**
     * 接集合数据，要加上@RequestParam注解
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteById(@RequestParam List<Long> ids)
    {
//        log.info("ids:{}",ids);
        setmealService.deleteWithSetmealDish(ids);

        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result<String> status(@PathVariable Integer status, @RequestParam List<Long> ids)
    {
        setmealService.updateStatus(status,ids);

        return Result.success();
    }

}
