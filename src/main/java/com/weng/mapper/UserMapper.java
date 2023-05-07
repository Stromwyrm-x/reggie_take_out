package com.weng.mapper;

import com.weng.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 24431
 * @description 针对表【user(用户信息)】的数据库操作Mapper
 * @createDate 2023-05-07 14:02:00
 * @Entity com.weng.entity.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User>
{

}




