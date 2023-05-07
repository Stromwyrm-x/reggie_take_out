package com.weng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.weng.common.Result;
import com.weng.common.util.BaseContext;
import com.weng.entity.AddressBook;
import com.weng.exception.BusinessException;
import com.weng.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 整体来说，地址簿管理的业务逻辑还算简单，可以自己独立完成(主要是没有dto)
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController
{
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public Result<String> add(@RequestBody AddressBook addressBook)
    {
        Long user_id = BaseContext.getCurrentId();
        addressBook.setUserId(user_id);
        addressBookService.save(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<AddressBook>> list()
    {
        Long user_id = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(user_id != null, AddressBook::getUserId, user_id);
        //个人认为如果每次查询都排序，会很乱所以干脆不排了
//        addressBookLambdaQueryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> addressBookList = addressBookService.list(addressBookLambdaQueryWrapper);
        return Result.success(addressBookList);
    }

    @PutMapping("/default")
    public Result<String> setDefault(@RequestBody AddressBook addressBook)
    {
        Long user_id = BaseContext.getCurrentId();
        //1.修改该用户id下的所有地址的isDefault为0
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        addressBookLambdaUpdateWrapper.set(AddressBook::getIsDefault, 0);
        addressBookLambdaUpdateWrapper.eq(AddressBook::getUserId, user_id);
        addressBookService.update(addressBookLambdaUpdateWrapper);

        //2.修改该地址的isDefault为1
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id)
    {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null)
        {
            return Result.success(addressBook);
        } else
        {
            return Result.error("没有找到该对象");
        }
    }

    @PutMapping
    public Result<String> update(@RequestBody AddressBook addressBook)
    {
        if (addressBook == null)
        {
            throw new BusinessException("地址信息不存在，请刷新重试");
        }
        addressBookService.updateById(addressBook);
        return Result.success();
    }

    @GetMapping("/default")
    public Result<AddressBook> getDefault()
    {
        Long user_id = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getIsDefault, 1);
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, user_id);
        AddressBook addressBook = addressBookService.getOne(addressBookLambdaQueryWrapper);

        if (addressBook == null)
        {
            return Result.error("没有找到该对象");
        } else
        {
            return Result.success(addressBook);
        }
    }

    @DeleteMapping
    public Result<String> deleteById(Long ids)
    {
        if (ids == null)
        {
            throw new BusinessException("地址信息不存在，请刷新重试");
        }
        AddressBook addressBook = addressBookService.getById(ids);
        if (addressBook == null)
        {
            throw new BusinessException("地址信息不存在，请刷新重试");
        }
        addressBookService.removeById(ids);
        return Result.success();

    }


}
