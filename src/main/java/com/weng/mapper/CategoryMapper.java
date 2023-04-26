package com.weng.mapper;

import com.weng.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 24431
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2023-04-26 22:14:34
* @Entity com.weng.entity.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




