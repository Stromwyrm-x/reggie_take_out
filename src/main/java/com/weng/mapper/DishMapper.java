package com.weng.mapper;

import com.weng.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 24431
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2023-04-27 15:10:58
* @Entity com.weng.entity.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




