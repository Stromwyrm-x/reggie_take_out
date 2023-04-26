package com.weng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.entity.Category;
import com.weng.service.CategoryService;
import com.weng.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 24431
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2023-04-26 22:14:34
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




