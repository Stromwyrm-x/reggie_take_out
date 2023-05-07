package com.weng.mapper;

import com.weng.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 24431
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2023-05-07 20:42:34
* @Entity com.weng.entity.AddressBook
*/

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}




