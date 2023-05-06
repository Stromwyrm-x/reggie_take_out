package com.weng.dto;

import com.weng.entity.Setmeal;
import com.weng.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealDto extends Setmeal
{

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
