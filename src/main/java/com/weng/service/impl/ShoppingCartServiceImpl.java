package com.weng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.entity.ShoppingCart;
import com.weng.service.ShoppingCartService;
import com.weng.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
 * @author 24431
 * @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
 * @createDate 2023-05-08 23:40:38
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService
{

}




