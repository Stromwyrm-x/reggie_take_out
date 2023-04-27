package com.weng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.entity.Dish;
import com.weng.service.DishService;
import com.weng.mapper.DishMapper;
import org.springframework.stereotype.Service;

/**
* @author 24431
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2023-04-27 15:10:58
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{

}




