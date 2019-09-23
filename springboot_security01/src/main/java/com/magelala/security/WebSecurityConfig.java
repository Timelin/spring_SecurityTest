package com.magelala.security;

import com.magelala.filter.VerifyFilter;
import com.magelala.security.email.EmailCodeAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import javax.xml.ws.WebServiceException;

/**
 * @ClassName:WebSecurityConfig
 * @Author:Timelin
 **/


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private  CustomUserDetailsService userDetailsService;

    @Autowired
    private  CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;


    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private EmailCodeAuthenticationSecurityConfig emailCodeAuthenticationSecurityConfig;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 如果token表不存在，使用下面语句可以初始化该表；若存在，请注释掉这条语句，否则会报错
       // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    /*
    * 注入自定义PermissionEvaluator*/
    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler1(){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return handler;

    }

    /*session剔出用户*/
    @Bean
    public SessionRegistry sessionRegistry(){
        return  new SessionRegistryImpl();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        //设置拦截器忽略文件夹，可以对静态资源放行

        web.ignoring().antMatchers("/css/**","/js/**");
    }


    // 权限功能
   @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.apply(smsCodeAuthenticationSecurityConfig).and()
                .apply(emailCodeAuthenticationSecurityConfig)
                .and().authorizeRequests()


                //如果有允许匿名的url,填在下面
                // 1 验证码
               .antMatchers("/getVerifyCode").permitAll()
                // 2 session时间限制
              //  .antMatchers("/login/invalid").permitAll()
                .antMatchers("/sms/**").permitAll()
                .antMatchers("/email/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // 设置登录页
                .formLogin().loginPage("/login")
                // 认证成功或者失败，将CustomAuthenticationFailureHandler，CustomAuthenticationSuccessHandler注入
                // 配置successHandler()和failureHandler()
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler).permitAll()
                // 设置登录成功页
               // .defaultSuccessUrl("/")
                // 设置登录成功页
              //  .failureUrl("/login/error").permitAll()
                // 自定义登陆用户名和密码参数，默认为username 和password
               // .usernameParameter("username")
              //  .passwordParameter("password")

                .and()
                .addFilterBefore(new VerifyFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/signout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()

              // 自动登录
               .and().rememberMe()
                .tokenRepository(persistentTokenRepository())
                // 有效时间：单位s
                .tokenValiditySeconds(600)
                .userDetailsService(userDetailsService)
                // session超时
                .and()
                .sessionManagement()
                .invalidSessionUrl("/login/invalid")
                // 限制最大登录数
                .maximumSessions(1)
                // 当达到最大值时，是否保留已经登录的用户
                .maxSessionsPreventsLogin(false)
               // 当达到最大值时，旧用户被踢出后的操作
               .expiredSessionStrategy(new CustomExpiredSessionStrategy())
               //踢出用户
               .sessionRegistry(sessionRegistry());

        // 关闭CSRF 跨域
        http.csrf().disable();
    }




}
