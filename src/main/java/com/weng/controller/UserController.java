package com.weng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.weng.common.Result;
import com.weng.common.util.SMSUtils;
import com.weng.common.util.ValidateCodeUtils;
import com.weng.entity.User;
import com.weng.service.UserService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController
{
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpServletRequest httpServletRequest)
    {
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone))
        {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("生成的验证码为:{}", code);
            //注意:短信签名和模板要相互关联才行，不能使用赠送的模板
//            SMSUtils.sendMessage("瑞吉外卖", "SMS_460710688", phone, code);

            //将code和phone存在session中，方便后续登陆时，拿填的code和得到的code对比
            httpServletRequest.getSession().setAttribute("code", code);
            httpServletRequest.getSession().setAttribute("phone", phone);
            return Result.success();
        }
        return Result.error("手机号不能为空!");
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpServletRequest httpServletRequest)
    {
        log.info("获取到的数据为:{}", map);
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();


        Object phoneInSession = httpServletRequest.getSession().getAttribute("phone");
        Object codeInSession = httpServletRequest.getSession().getAttribute("code");

        if (codeInSession != null && codeInSession.equals(code) && phoneInSession != null && phoneInSession.equals(phone))
        {
            //因为要返回一个user对象，所以查找数据库中的user表，如果没有，则注册一个
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            if (user == null)
            {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            //存入session,便于filter来进行过滤
            httpServletRequest.getSession().setAttribute("user", user.getId());
            return Result.success(user);
        }

        return Result.error("手机号或验证码不匹配!");
    }

    @PostMapping("/loginout")
    public Result<String> logout(HttpServletRequest httpServletRequest)
    {
        httpServletRequest.getSession().removeAttribute("user");
        httpServletRequest.getSession().removeAttribute("code");
        httpServletRequest.getSession().removeAttribute("phone");
        return Result.success();
    }

    /**
     * 目前前端还未实现
     * @param phone
     * @return
     */
    @GetMapping
    public Result<User> getById(String phone)
    {
        LambdaQueryWrapper<User>userLambdaQueryWrapper=new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone,phone);
        User user = userService.getOne(userLambdaQueryWrapper);

        return Result.success(user);
    }

    /**
     * 目前前端还未实现
     * @param user
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody User user)
    {
        userService.updateById(user);
        return Result.success();
    }

}
