package com.weng.common.util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.weng.common.util.BaseContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * 公共字段自动填充
 */
@Configuration
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler
{
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Override
    public void insertFill(MetaObject metaObject)
    {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long currentId = BaseContext.getCurrentId();
        log.info("session里的id为:{}",currentId);
        metaObject.setValue("updateUser", currentId);
        metaObject.setValue("createUser", currentId);
//        metaObject.setValue("createUser", httpServletRequest.getSession().getAttribute("employee"));
//        metaObject.setValue("updateUser", httpServletRequest.getSession().getAttribute("employee"));


    }

    @Override
    public void updateFill(MetaObject metaObject)
    {
        Long currentId = BaseContext.getCurrentId();
        log.info("session里的id为:{}",currentId);
        metaObject.setValue("updateUser",currentId);
        metaObject.setValue("updateTime", LocalDateTime.now());
//        metaObject.setValue("updateUser", httpServletRequest.getSession().getAttribute("employee"));
    }
}
