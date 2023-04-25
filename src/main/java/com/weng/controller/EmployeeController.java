package com.weng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weng.common.Result;
import com.weng.entity.Employee;
import com.weng.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController
{
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param employee
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Employee employee, HttpServletRequest httpServletRequest)
    {
        //1、将页面提交的密码进行md5加密处理
        String password=employee.getPassword();
        log.info("password:{}",password);
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("password after md5:{}",password);
        //2、根据页面提交的用户名来查数据库
        LambdaQueryWrapper<Employee> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employee_db = employeeService.getOne(lambdaQueryWrapper);

        //3、如果没有查询到则返回失败结果
        if (employee_db==null)
        {
            return Result.error("用户名不存在!");
        }

        //4、比对密码，如果不一致则返回失败结果
        if (!employee_db.getPassword().equals(password))
        {
            return Result.error("密码错误!");
        }

        //5、查看员工状态，如果已禁用状态，则返回员工已禁用结果
        if (employee_db.getStatus()==0)
        {
            return Result.error("该员工账号已被禁用!");
        }

        //6、登录成功，将用户id存入Session并返回成功结果
        httpServletRequest.getSession().setAttribute("employee",employee_db.getId());
        return Result.success(employee_db);

    }

    /**
     * 员工退出登录
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

}
