package com.weng.mapper;

import com.weng.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 24431
 * @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
 * @createDate 2023-05-09 19:12:46
 * @Entity com.weng.entity.OrderDetail
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail>
{

}




