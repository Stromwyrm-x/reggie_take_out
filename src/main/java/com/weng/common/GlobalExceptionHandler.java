package com.weng.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{
    /**
     * 处理数据库异常,employee的username重复
     * @param e
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException e)
    {
        log.error(e.getMessage());
        if (e.getMessage().contains("Duplicate entry"))
        {
            String[] message = e.getMessage().split(" ");
            return Result.error(message[2]+"已存在!");
        }
        return Result.error("未知错误!");
    }
}
