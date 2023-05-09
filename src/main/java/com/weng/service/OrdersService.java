package com.weng.service;

import com.weng.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 24431
 * @description 针对表【orders(订单表)】的数据库操作Service
 * @createDate 2023-05-09 19:12:46
 */
public interface OrdersService extends IService<Orders>
{
    @Transactional
    void submit(Orders orders);
}
