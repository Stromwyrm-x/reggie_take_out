package com.weng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 24431
 * @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Mapper
 * @createDate 2023-04-28 10:37:02
 * @Entity com.weng.DishFlavor
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor>
{
}




