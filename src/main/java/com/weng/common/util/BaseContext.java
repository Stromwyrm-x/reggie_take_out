package com.weng.common.util;

/**
 * 基于ThreadLocal封装的工具类，用来保存和获取session里存的用户id
 */
public class BaseContext
{
    //会在第一次调用BaseContext的方法时，执行变量赋值。之后这个threadLocal的哈希值不会变
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static Long getCurrentId()
    {
        return threadLocal.get();
    }
    public static void setCurrentId(Long id)
    {
        threadLocal.set(id);
    }
}
