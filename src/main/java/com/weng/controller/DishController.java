package com.weng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weng.common.Result;
import com.weng.dto.DishDto;
import com.weng.entity.Category;
import com.weng.entity.Dish;
import com.weng.service.CategoryService;
import com.weng.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController
{
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping
    public Result<String> add(@RequestBody DishDto dishDto)
    {
        log.info("接收到的dto:{}", dishDto);
        dishService.addWithFlavor(dishDto);

        //根据传入的categoryId删除对应的缓存
        String key = "dish_" + dishDto.getCategoryId();
        redisTemplate.delete(key);

        return Result.success();
    }

    @GetMapping("/page")
    public Result<Page<DishDto>> page(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "5") Integer pageSize,
                                      String name)
    {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage, lambdaQueryWrapper);

        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

        //获取原records数据
        List<Dish> records = dishPage.getRecords();

        //遍历每一条records数据
        List<DishDto> list = records.stream().map((item) ->
        {
            DishDto dishDto = new DishDto();
            //将数据赋给dishDto对象
            BeanUtils.copyProperties(item, dishDto);
            //然后获取一下dish对象的category_id属性
            Long categoryId = item.getCategoryId();  //分类id
            //根据这个属性，获取到Category对象（这里需要用@Autowired注入一个CategoryService对象）
            Category category = categoryService.getById(categoryId);
            //随后获取Category对象的name属性，也就是菜品分类名称
            String categoryName = category.getName();
            //最后将菜品分类名称赋给dishDto对象就好了
            dishDto.setCategoryName(categoryName);
            //结果返回一个dishDto对象
            return dishDto;
            //并将dishDto对象封装成一个集合，作为我们的最终结果
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return Result.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public Result<DishDto> get(@PathVariable Long id)
    {
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return Result.success(dishDto);
    }


    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto)
    {

        dishService.updateWithFlavor(dishDto);

        //删除所有的缓存，因为dish类型可以改变。修改菜品的时候不用删除套餐的缓存，因为套餐的list方法不会展示具体的dish信息
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        return Result.success();
    }

    /**
     * 根据分类id查找dish的集合
     *
     * @return
     */
//    @GetMapping("/list")
//    public Result<List<Dish>> listByCategoryId(Dish dish)
//    {
//        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        //只显示启售状态的菜品
//        lambdaQueryWrapper.eq(Dish::getStatus, 1);
//        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        lambdaQueryWrapper.orderByAsc(Dish::getSort);
//        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(lambdaQueryWrapper);
//        return Result.success(list);
//    }
    @GetMapping("/list")
    public Result<List<DishDto>> listByCategoryId(Dish dish)
    {
        //查询是否有缓存，如果有直接返回
        List<DishDto> dtoList;

        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        String key = "dish_" + dish.getCategoryId();
        dtoList = (List<DishDto>) opsForValue.get(key);
        if (dtoList != null)
        {
            return Result.success(dtoList);
        }

        //没有缓存，则进行数据库查询
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //只显示启售状态的菜品
        lambdaQueryWrapper.eq(Dish::getStatus, 1);
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(lambdaQueryWrapper);

        dtoList = list.stream().map(item ->
        {
            DishDto dishDto = dishService.getByIdWithFlavor(item.getId());
            return dishDto;
        }).collect(Collectors.toList());

        //如果没有缓存，则将数据库查询得到的数据放入缓存
        opsForValue.set(key, dtoList, 60, TimeUnit.MINUTES);

        return Result.success(dtoList);
    }

    @DeleteMapping
    public Result<String> deleteById(@RequestParam List<Long> ids)
    {
        dishService.deleteWithFlavor(ids);

        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result<String> status(@PathVariable Integer status,
                                 @RequestParam(name = "ids") List<Long> ids)
    {

        dishService.updateStatus(status, ids);
        //删除dish所在的分类的下的缓存
        ids.stream().forEach(dishId ->
        {
            Dish dish = dishService.getById(dishId);
            String key = "dish_" + dish.getCategoryId();
            redisTemplate.delete(key);
        });

        return Result.success();
    }


}
