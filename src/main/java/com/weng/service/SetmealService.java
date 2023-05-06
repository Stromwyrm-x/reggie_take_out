package com.weng.service;

import com.weng.dto.SetmealDto;
import com.weng.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 24431
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2023-04-27 15:10:58
*/
public interface SetmealService extends IService<Setmeal> {

    @Transactional
    void addWithSetmealDish(SetmealDto setmealDto);

    @Transactional
    SetmealDto getByIdWithSetmealDish(Long id);

    @Transactional
    void updateWithSetmealDish(SetmealDto setmealDto);

    @Transactional
    void deleteWithSetmealDish(List<Long> ids);

    @Transactional
    void updateStatus(Integer status, List<Long> ids);
}
