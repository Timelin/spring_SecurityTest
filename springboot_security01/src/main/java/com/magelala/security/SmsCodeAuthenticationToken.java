package com.magelala.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * @ClassName:SmsCodeAuthenticationToken
 * @Author:Timelin
 * 短信登录 短信登录 AuthenticationToken，模仿 UsernamePasswordAuthenticationToken 实现
 **/

public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken  {



    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private final Object principal;
    private Object credentials;

    /*构建一个没有鉴权过的 SmsCodeAuthenticationToken*/
    public SmsCodeAuthenticationToken( Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }
    /*构建一个有鉴权过的 SmsCodeAuthenticationToken*/
    public SmsCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }


    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
