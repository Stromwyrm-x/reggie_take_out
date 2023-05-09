package com.weng.mapper;

import com.weng.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 24431
 * @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
 * @createDate 2023-05-08 23:40:38
 * @Entity com.weng.entity.ShoppingCart
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart>
{

}




