package com.weng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.entity.DishFlavor;
import com.weng.service.DishFlavorService;
import com.weng.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author 24431
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2023-04-28 10:37:02
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




