package com.weng.mapper;

import com.weng.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 24431
 * @description 针对表【orders(订单表)】的数据库操作Mapper
 * @createDate 2023-05-09 19:12:46
 * @Entity com.weng.entity.Orders
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders>
{

}




