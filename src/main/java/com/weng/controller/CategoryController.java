package com.weng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weng.common.Result;
import com.weng.entity.Category;
import com.weng.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController
{
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> add(@RequestBody Category category)
    {
        categoryService.save(category);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<Page<Category>> page(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "5") Integer pageSize)
    {
        Page<Category> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,lambdaQueryWrapper);
        return Result.success(pageInfo);
    }

    @DeleteMapping
    public Result<String> deleteById(Long ids)
    {
        log.info("删除的id为：{}",ids);
        categoryService.removeById(ids);
        return Result.success();
    }

<<<<<<< HEAD
    @PutMapping
    public Result<String> update (@RequestBody Category category)
    {
        categoryService.updateById(category);
        return Result.success();
    }
=======
>>>>>>> c2065cb (删除分类功能完善，当分类关联着菜品或套餐时，无法删除该分类)
}