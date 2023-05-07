package com.weng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.entity.User;
import com.weng.service.UserService;
import com.weng.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author 24431
 * @description 针对表【user(用户信息)】的数据库操作Service实现
 * @createDate 2023-05-07 14:02:01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService
{

}




