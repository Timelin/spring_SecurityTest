package com.magelala.security.email;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import javax.security.auth.Subject;
import java.util.Collection;

/**
 * 邮箱验证码登录
 * @ClassName:EmailCodeAuthenticationToken
 * @Author:Timelin
 **/
public class EmailCodeAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    // 这里principal 指的是Email地址（没有认证的时候）
    private final Object principal;


    /*构建一个没有鉴权过的 EmailCodeAuthenticationToken*/
    public EmailCodeAuthenticationToken( Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }
    /*构建一个有鉴权过的 EmailCodeAuthenticationToken*/
    public EmailCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

//如果是Set认证状态，就无情的给一个异常，意思是：
    //不要在这里设置已认证，不要在这里设置已认证，不要在这里设置已认证
    //应该从构造方法里创建，别忘了要带上用户信息和权限列表哦
    //原来如此，是避免犯错吧
    /*
上面是复制的 接下来个人理解->
第一次 使用token 是未认证的 principal 里存放的是用户名
第二次 是认证的 该token 里面 还存放了 该用户的权限
springsecurity 通过这个标志来判断 是否被认证

     */


    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

/*-------------父类抽象方法----------*/
    public Object getPrincipal() {
    return this.principal;
}
    @Override
    public Object getCredentials() {
        return null;
    }
    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
