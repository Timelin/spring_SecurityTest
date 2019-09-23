package com.magelala.filter;

import org.springframework.security.authentication.DisabledException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @ClassName:VerifyFilter
 * @Author:Timelin
 **/
// 过滤验证码
public class VerifyFilter extends OncePerRequestFilter {

    private  static  final PathMatcher pathMatcher= new AntPathMatcher();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 判断请求方式是否正确
        if (isProtectedUrl(request)) {
            // 获取前端的验证码参数
            String verifyCode = request.getParameter("verifyCode");
            //校验验证码
            if (!validateVerify(verifyCode)){
                // 手动设置异常
                request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION",new DisabledException("验证码输入错误"));
                // 转发到错误URL
                request.getRequestDispatcher("/login/error").forward(request,response);

            }else {
                filterChain.doFilter(request,response);
            }
        }else {
            filterChain.doFilter(request,response);
        }
    }


    // 校验用户输入的验证码是否符合
    private boolean validateVerify(String inputverifyCode) {
        //获取当前线程绑定的request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 不区分大小写
        // 这个validateCode 是在servlet 中存入session的名字
        String validateCode = ((String) request.getSession().getAttribute("validateCode")).toLowerCase();
        inputverifyCode = inputverifyCode.toLowerCase();
        System.out.println("验证码："+validateCode+"用户输入："+inputverifyCode);
        return validateCode.equals(inputverifyCode);

    }


    // 拦截/login 的post 请求
    private boolean isProtectedUrl(HttpServletRequest request){
        return "POST".equals(request.getMethod())&& pathMatcher.match("/login",request.getServletPath());
    }
}
