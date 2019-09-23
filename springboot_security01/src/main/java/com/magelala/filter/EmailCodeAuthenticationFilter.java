package com.magelala.filter;

import com.magelala.security.SmsCodeAuthenticationToken;
import com.magelala.security.email.EmailCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * 邮箱验证码登录：对特定邮箱登录URL进行过滤，封装token交由provide进行认证
 * @ClassName:EmailCodeAuthenticationFilter
 * @Author:Timelin
 **/
public class EmailCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    /*前端传来的 参数名*/
    private  final  String DEFAULT_EMAIL_NAME="email";
    private  final  String DEFAULT_EMAIL_CODE="emailCode";

    private  String  emailParameter = DEFAULT_EMAIL_NAME;


    /* 是否POST方式*/
    private boolean postOnly = true;

    /*
    * 父类中是调用 setFilterProcessesUrl(defaultFilterProcessesUrl);方法 创建可以通过的url
    * 精确绑定url*/
    protected EmailCodeAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }


    /*
    * 通过传入的参数创建匹配器即 Filter过滤url
    * */
    public EmailCodeAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(new AntPathRequestMatcher("/email/login","POST"));
    }

    /*给权限
    * filter 获得 用户名（邮箱） 和 密码（验证码） 装配到token上，
    * 然后把token 交给 provider 进行授权*/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }else {
            String email = getEmail(request);
            if (email == null) {
                email = "";
            }

            email = email.trim();
            // 校验验证码 如果不相等 让token出差 ，然后走springsecurity 错误的流程
            boolean flag = isCodeEquals(request);

            // 封装token
            EmailCodeAuthenticationToken token = null;
            if(flag){
                token = new EmailCodeAuthenticationToken(email);

            }else {
                token = new EmailCodeAuthenticationToken("error");
            }

            // Allow subclasses to set the "details" property
            setDetails(request, token);
            return this.getAuthenticationManager().authenticate(token);


        }

    }

    /*
    * 校验验证码*/
    private boolean isCodeEquals(HttpServletRequest request) {
        HttpSession session = request.getSession();
        /*String inputCode = request.getParameter(DEFAULT_EMAIL_CODE);
        System.out.println("前端输入的验证码code1===========："+inputCode);
        Map<String, Object> emailCode = (Map<String, Object>) session.getAttribute(DEFAULT_EMAIL_CODE);
        int code2 =(int) emailCode.get("code");
        System.out.println("后台生成的验证码code2===========："+code2);
        String applycode= String.valueOf(code2);

        try {
            return inputCode.equals(applycode);
        } catch (Exception e) {
            return false;
        }*/
       String inputCoden = request.getParameter("DEFAULT_EMAIL_CODE");

        Map<String, Object> emailCode = (Map<String, Object>) session.getAttribute(DEFAULT_EMAIL_CODE);

        if(emailCode ==null){
            throw new BadCredentialsException("未检测到申请验证码");
        }

        String inputCode = request.getParameter(DEFAULT_EMAIL_CODE);

        System.out.println("前端输入的验证码code1===========："+inputCode);
        int code = (int) emailCode.get("code");


        System.out.println("后台生成的验证码code2===========："+code);

        try {
            if(code != Integer.parseInt(inputCode)){
                throw new BadCredentialsException("验证码错误");
            }
            return true;
        } catch (Exception e) {
           return false;
        }



    }

    /*
    * 获取前端传来的 Email信息*/
    private String getEmail(HttpServletRequest request) {
        return request.getParameter(emailParameter);
    }

    /*
    * 获取 头部信息 让合适的provider来验证他
    * */
    protected void setDetails(HttpServletRequest request, EmailCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
