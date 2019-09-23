package com.magelala.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 短信登录鉴权 Provider , 要求实现 AuthenticcationProvider 接口
 * @ClassName:SmsCodeAuthenticationProvider
 * @Author:Timelin
 **/
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        String mobile = (String)authenticationToken.getPrincipal();

        checkSmsCode(mobile);
        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);

        // 此时鉴权成功后 ，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails.getAuthorities(),userDetails);

        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    // 校验验证码
    private void checkSmsCode(String mobile) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String inputCoden = request.getParameter("smsCode");

        Map<String, Object> smsCode = (Map<String, Object>) request.getSession().getAttribute("smsCode");
        if(smsCode ==null){
            throw new BadCredentialsException("未检测到申请验证码");
        }

        String applymobile = (String) smsCode.get("mobile");

        int code = (int) smsCode.get("code");
        if(!applymobile.equals(mobile)){
            throw new BadCredentialsException("申请的手机号码与登录手机号码不一致");

        }
        if(code != Integer.parseInt(inputCoden)){
            throw new BadCredentialsException("验证码错误");

        }

    }

    @Override
    public boolean supports(Class<?> authenticate) {
        // 判断 authentication 是不是 SmsCodeAuthentricationToken 的子类或子接口

        return SmsCodeAuthenticationToken.class.isAssignableFrom(authenticate);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
