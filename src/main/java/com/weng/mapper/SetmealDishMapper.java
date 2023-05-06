package com.weng.mapper;

import com.weng.entity.SetmealDish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 24431
* @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Mapper
* @createDate 2023-05-05 20:17:30
* @Entity com.weng.SetmealDish
*/
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {

    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> selectBySetMealId(Long id);
}




