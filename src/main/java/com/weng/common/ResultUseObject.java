package com.weng.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultUseObject
{
    private Integer code;
    private String message;
    private Object data;//会增加类型转换的风险

    public static ResultUseObject success()
    {
        return new ResultUseObject(1,"success",null);
    }
    public static ResultUseObject success(Object data)
    {
        return new ResultUseObject(1,"success",data);
    }
    public static ResultUseObject error(String message)
    {
        return new ResultUseObject(0,message,null);
    }

}
