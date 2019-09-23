package com.magelala.security.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 邮箱验证码登录失败
 * @ClassName:MyAuthenticationFailHandler
 * @Author:Timelin
 **/
/*
这里加上scope的原因是
spring中autowired装配的对象是单例的：
那么问题来了，
因为我既有 邮箱登录 又有 密码登录，这连个我同时使用用一个handler,
但是他们失败后跳转的url 又是不一样的，所以通过一个tar 属性动态的绑定不同的handler
如果是单例的，那么后一个配置的tar 会覆盖 前一个配置tar，
从而导致 邮件也好，手机也好 都跳转到 同一个位置， 这是我们不想要的，
所以使用 多例 的@Scope 注解来绑定不同的handler

* */
@Component
@Scope("prototype")
public class MyAuthenticationFailHandler  extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());


   // 跳转


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        super.setDefaultFailureUrl("/login/error/");
        super.onAuthenticationFailure(request, response, exception);
    }


}
