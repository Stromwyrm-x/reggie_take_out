package com.weng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weng.common.Result;
import com.weng.common.util.BaseContext;
import com.weng.dto.OrdersDto;
import com.weng.entity.OrderDetail;
import com.weng.entity.Orders;
import com.weng.service.OrderDetailService;
import com.weng.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController
{
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders)
    {

        ordersService.submit(orders);
        return Result.success();
    }

    /**
     * 移动端查询历史订单
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public Result<Page<OrdersDto>> page_frontend(@RequestParam(defaultValue = "1") Integer page,
                                                 @RequestParam(defaultValue = "5") Integer pageSize)
    {
        Long user_id = BaseContext.getCurrentId();
        Page<OrdersDto> ordersDtoPage = new Page<>(page, pageSize);
        Page<Orders> ordersPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId, user_id);
        ordersLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);
//        List<Orders> ordersList = ordersService.list(ordersLambdaQueryWrapper);
        //只有通过page方法调用查询后，才能为之中的字段赋值
        ordersService.page(ordersPage, ordersLambdaQueryWrapper);
        BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records");

        List<Orders> ordersList = ordersPage.getRecords();

        List<OrdersDto> ordersDtoList = ordersList.stream().map(item ->
        {
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //select * from order_detail where order_id = #{number};
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getNumber());

            List<OrderDetail> orderDetailList = orderDetailService.list(orderDetailLambdaQueryWrapper);
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            ordersDto.setOrderDetails(orderDetailList);
            return ordersDto;

        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(ordersDtoList);

        return Result.success(ordersDtoPage);
    }

    @GetMapping("/page")
    public Result<Page<Orders>> page_backend(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "5") Integer pageSize,
                                             String number,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime)
    {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);

        ordersLambdaQueryWrapper.eq(number != null, Orders::getNumber, number);
        ordersLambdaQueryWrapper.gt(beginTime != null, Orders::getOrderTime, beginTime)
                                .lt(endTime != null, Orders::getOrderTime, endTime);
        ordersService.page(ordersPage,ordersLambdaQueryWrapper);

        return Result.success(ordersPage);
    }

    /**
     * 修改订单状态
     */
    @PutMapping
    public Result<String> updateStatus(@RequestBody Orders orders)
    {

        ordersService.updateById(orders);
        return Result.success();
    }


}
