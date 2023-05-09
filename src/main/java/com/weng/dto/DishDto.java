package com.weng.dto;

import com.weng.entity.Dish;
import com.weng.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    // 用来移动端返回的，卧槽！！！用于表示对应的setmeal当中的dish的份数
    private Integer copies;


}
