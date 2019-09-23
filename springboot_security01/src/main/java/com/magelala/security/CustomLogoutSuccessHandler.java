package com.magelala.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName:CustomLogoutSuccessHandler
 * @Author:Timelin
 **/
@Component
public class CustomLogoutSuccessHandler  implements LogoutSuccessHandler{
     private Logger logger = LoggerFactory.getLogger(getClass());

    // 处理退出登录
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String username = ((User) authentication.getPrincipal()).getUsername();
        logger.info("退出成功，用户名：{}",username);

        // 重定向到登录页
        response.sendRedirect("/login");

    }
}
