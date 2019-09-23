package com.magelala.security.email;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 邮箱验证码登录
 * @ClassName:EmailCodeAuthenticationProvider
 * @Author:Timelin
 **/
public class EmailCodeAuthenticationProvider  implements AuthenticationProvider{

    private UserDetailsService userDetailsService;

    public EmailCodeAuthenticationProvider(UserDetailsService userDetailsService) {

        this.userDetailsService=userDetailsService;
    }


    // 认证
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailCodeAuthenticationToken token = (EmailCodeAuthenticationToken) authentication;
        String email = (String) token.getPrincipal();

        //UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UserDetails user = userDetailsService.loadUserByUsername(email);
        System.out.println(token.getPrincipal());
        if(user ==null){
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        System.out.println(user.getAuthorities());
        // 此时鉴权成功后， 应当重新 new 一个拥有鉴权的 authenticationResult 返回
       /* EmailCodeAuthenticationToken authenticationResult = new EmailCodeAuthenticationToken(userDetails.getAuthorities(),userDetails);
        authenticationResult.setDetails(token.getDetails());
        return authenticationResult;*/
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        /*Details 中包含了 ip地址、sessionId 等属性*/
        result.setDetails(token.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }



    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
