package com.weng.mapper;

import com.weng.entity.Setmeal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 24431
 * @description 针对表【setmeal(套餐)】的数据库操作Mapper
 * @createDate 2023-04-27 15:10:58
 * @Entity com.weng.entity.Setmeal
 */
@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal>
{

}




