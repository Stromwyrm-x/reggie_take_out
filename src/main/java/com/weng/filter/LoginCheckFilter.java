package com.weng.filter;

import com.google.gson.Gson;
import com.weng.common.Result;
import com.weng.common.util.BaseContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
@Slf4j
@Component
public class LoginCheckFilter implements Filter
{

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Autowired
    private Gson gson;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

//        1、获取本次请求的URI
        String requestURI = httpServletRequest.getRequestURI();
        //页面想看就看，重点是不让看页面上的数据（数据会发送动态请求）。主要是拦截controller的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/sendMsg"
        };

//        2、判断本次请求是否需要处理(如果请求的uri在urls数组中，直接放行即可)
        boolean check = check(urls, requestURI);

//        3、如果不需要处理，则直接放行
        if (check)
        {
            log.info("本次请求的地址是：{}，不需要处理",requestURI);
            chain.doFilter(request, response);
            return;
        }

//        4-1、判断管理端登录状态，如果已登录，则直接放行
        if (httpServletRequest.getSession().getAttribute("employee") != null)
        {
            log.info("管理端用户已登录，id为{}",httpServletRequest.getSession().getAttribute("employee"));
            //将登录的session中存储的id存入ThreadLocal中
            Long employee_id = (Long) httpServletRequest.getSession().getAttribute("employee");
            BaseContext.setCurrentId(employee_id);

            chain.doFilter(request, response);
            return;
        }
//        4-2、判断移动端登录状态，如果已登录，则直接放行
        if (httpServletRequest.getSession().getAttribute("user")!=null)
        {
            log.info("移动端用户已登录,id为:{}",httpServletRequest.getSession().getAttribute("user"));

            Long user_id= (Long) httpServletRequest.getSession().getAttribute("user");
            BaseContext.setCurrentId(user_id);

            chain.doFilter(request,response);
            return;
        }


//        5、如果未登录则返回未登录结果（通过输出流方式向客户端页面响应数据）
//        前端的request.js中定义了一个拦截器，如果收到code=0,msg="NOTLOGIN"则会跳转到登录页面
        log.info("用户未登录");
        //这里可以使用jackson或gson。因为fastjson会吃掉为null的data!!!!
        httpServletResponse.getWriter().write(gson.toJson(Result.error("NOTLOGIN")));
//        httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(Result.error("NOTLOGIN")));
//        httpServletResponse.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));

        return;

    }

    public boolean check(String[] urls, String requestURI)
    {
        for (String url : urls)
        {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match)
            {
                //匹配
                return true;
            }
        }
        //不匹配
        return false;
    }


}
