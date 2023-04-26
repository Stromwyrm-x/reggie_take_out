package com.weng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weng.common.Result;
import com.weng.entity.Employee;
import com.weng.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController
{
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param employee
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Employee employee, HttpServletRequest httpServletRequest)
    {
        //1、将页面提交的密码进行md5加密处理
        String password = employee.getPassword();
        log.info("password:{}", password);
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("password after md5:{}", password);
        //2、根据页面提交的用户名来查数据库
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee employee_db = employeeService.getOne(lambdaQueryWrapper);

        //3、如果没有查询到则返回失败结果
        if (employee_db == null)
        {
            return Result.error("用户名不存在!");
        }

        //4、比对密码，如果不一致则返回失败结果
        if (!employee_db.getPassword().equals(password))
        {
            return Result.error("密码错误!");
        }

        //5、查看员工状态，如果已禁用状态，则返回员工已禁用结果
        if (employee_db.getStatus() == 0)
        {
            return Result.error("该员工账号已被禁用!");
        }

        //6、登录成功，将用户id存入Session并返回成功结果
        httpServletRequest.getSession().setAttribute("employee", employee_db.getId());
        return Result.success(employee_db);

    }

    /**
     * 员工退出登录
     *
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest httpServletRequest)
    {
        //清理Session中保存的当前员工登录的id
        httpServletRequest.getSession().removeAttribute("employee");
        return Result.success();
    }

    /**
     * 会调用MyMetaObjectHandler,公共字段自动填充
     * @param httpServletRequest
     * @param employee
     * @return
     */
    @PostMapping
    public Result<String> add(HttpServletRequest httpServletRequest, @RequestBody Employee employee)
    {
        log.info("接受到的员工信息为:{}",employee);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser((Long) httpServletRequest.getSession().getAttribute("employee"));
//        employee.setUpdateUser((Long) httpServletRequest.getSession().getAttribute("employee"));
        employeeService.save(employee);
        return Result.success();
    }


    @GetMapping("/page")
    public Result<Page<Employee>> page(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "5") Integer pageSize,
                                       String name)
    {
        log.info("传来的分页参数:page={},pageSize={},name={}",page,pageSize,name);
        //使用mp的分页查询工具
        Page<Employee> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //设置查询条件
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        //会把查询到的信息直接放到pageInfo中
        employeeService.page(pageInfo,lambdaQueryWrapper);
        return Result.success(pageInfo);
    }

    /**
     * 会调用MyMetaObjectHandler,公共字段自动填充
     * @param httpServletRequest
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> update(HttpServletRequest httpServletRequest,@RequestBody Employee employee)
    {
        LambdaQueryWrapper<Employee> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) httpServletRequest.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id)
    {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }


}
