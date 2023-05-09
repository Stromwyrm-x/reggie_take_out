package com.weng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.common.util.BaseContext;
import com.weng.entity.*;
import com.weng.exception.BusinessException;
import com.weng.mapper.*;
import com.weng.service.OrderDetailService;
import com.weng.service.OrdersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 24431
 * @description 针对表【orders(订单表)】的数据库操作Service实现
 * @createDate 2023-05-09 19:12:46
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService
{

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void submit(Orders orders)
    {
        Long user_id = BaseContext.getCurrentId();
        //根绝用户id查询购物车、用户信息
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, user_id);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(shoppingCartLambdaQueryWrapper);
        if (shoppingCartList == null)
        {
            throw new BusinessException("购物车不能为空!");
        }

        User user = userMapper.selectById(user_id);
        //根据传过来的addressBookId,查询地址信息
        AddressBook addressBook = addressBookMapper.selectById(orders.getAddressBookId());
        if (addressBook == null)
        {
            throw new BusinessException("默认地址有误，不能下单!");
        }

        //获取订单号,金额
        long order_number = IdWorker.getId();
        AtomicInteger amount = new AtomicInteger(0);

        //向订单明细表设置属性

        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(item ->
        {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(item.getName());
            orderDetail.setOrderId(order_number);
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());
            orderDetail.setImage(item.getImage());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //添加订单表
        orders.setNumber(String.valueOf(order_number));
        orders.setStatus(2);
        orders.setUserId(user_id);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserName(user.getName());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName()) +
                (addressBook.getCityName() == null ? "" : addressBook.getCityName()) +
                (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName()) +
                (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        orders.setConsignee(addressBook.getConsignee());

        ordersMapper.insert(orders);

        //添加订单明细表
        orderDetailService.saveBatch(orderDetailList);

        //清空购物车
        shoppingCartMapper.delete(shoppingCartLambdaQueryWrapper);

    }
}




