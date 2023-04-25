package com.weng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>
{
}
