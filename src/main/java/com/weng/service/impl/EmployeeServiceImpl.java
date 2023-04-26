package com.weng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.entity.Employee;
import com.weng.mapper.EmployeeMapper;
import com.weng.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService
{
    @Autowired
    private EmployeeMapper employeeMapper;

}
