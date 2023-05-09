package com.weng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.entity.OrderDetail;
import com.weng.service.OrderDetailService;
import com.weng.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 24431
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2023-05-09 19:12:46
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




