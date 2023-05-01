package com.weng.service;

import com.weng.dto.DishDto;
import com.weng.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 24431
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2023-04-27 15:10:58
*/
public interface DishService extends IService<Dish> {

    @Transactional
    void addWithFlavor(DishDto dishDto);

    @Transactional
    DishDto getByIdWithFlavor(Long id);

    @Transactional
    void updateWithFlavor(DishDto dishDto);
}
