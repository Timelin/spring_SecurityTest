package com.magelala.controller;

import com.magelala.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:SysUserController
 * @Author:Timelin
 **/
@Controller
public class LoginController {
    private Logger logger =LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private EmailUtil emailUtil;

    // 踢出用户
    @GetMapping("/kick")
    @ResponseBody
    public String removeUserSessionByUsername(@RequestParam String username){
        int count =0;
        // 参数一：获取session中所有的用户信息
        List<Object> users = sessionRegistry.getAllPrincipals();
        // 遍历session中所有的用户信息
        for (Object principal :users){
            // 判断用户是否一致
            if(principal instanceof User){
                String principalName = ((User) principal).getUsername();
                // 判断用户名是否一致
                if (principalName.equals(username)){
                    // 参数二： 是否包含过期的Session
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
                    if (null != sessionsInfo && sessionsInfo.size()>0){
                        for (SessionInformation sessionInformation :sessionsInfo){
                            sessionInformation.expireNow();
                            count++;
                        }
                    }



                }
            }
        }
        return "操作成功，清理session供"+count+"个";

    }

    @RequestMapping("/")
    public String showHome(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("当前登录用户："+name);
        return "home.html";
    }
    @RequestMapping("/login/error")
    public void loginError(HttpServletRequest request, HttpServletResponse response){

        //解决乱码问题
        response.setContentType("text/html;charset=utf-8");
        AuthenticationException exception = (AuthenticationException) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        try {
            response.getWriter().write(exception.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/login")
    public String showLogin(){
        return "login.html";
    }

    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String printAdmin(){
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";

    }

    @RequestMapping("/admin/r")
    @ResponseBody
    @PreAuthorize("hasPermission('/admin','r')")
    public String printAdminR(){
        return "如果你看见这句话，说明你访问/admin路径具有r权限";

    }
    @RequestMapping("/admin/c")
    @ResponseBody
    @PreAuthorize("hasPermission('/admin','c')")
    public String printAdminC(){
        return "如果你看见这句话，说明你访问/admin路径具有c权限";

    }

    @RequestMapping("/user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public String printUser(){
        return "如果你看见这句话，说明你有ROLE_USER角色";
    }


    @RequestMapping("/login/invalid")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String invalid(){
        return "Session 已经过期，请重新登录";
    }



    @GetMapping("/me")
    @ResponseBody
    public Object me(){
        return  SecurityContextHolder.getContext().getAuthentication();
    }


    // 短信登录生成验证码
    @RequestMapping("/sms/code")
    @ResponseBody
    public void   sms(String mobile, HttpSession session,HttpServletResponse response) throws IOException {
        int code = (int) Math.ceil(Math.random()* 9000+1000);
        Map<String,Object> map = new HashMap<>(16);
        map.put("mobile",mobile);
        map.put("code",code);
        session.setAttribute("smsCode",map);

        // 发送验证码到指定手机号
        emailUtil.sendMail(code,mobile);

        logger.info("{}: 为{} 设置短信验证码：{}",session.getId(),mobile,code);

        response.sendRedirect("/login");
    }

    // 邮箱登录生成验证码
    @RequestMapping("/email/code")
    @ResponseBody
    public void   email(String email, HttpSession session,HttpServletResponse response) throws IOException {
        int code = (int) Math.ceil(Math.random()* 9000+1000);
        Map<String,Object> map = new HashMap<>(16);
        map.put("email",email);
        map.put("code",code);
        session.setAttribute("emailCode",map);


        // 发送验证码到指定邮箱
       emailUtil.sendMail(code,email);


        logger.info("{}: 为{} 设置邮箱验证码：{}",session.getId(),email,code);

        response.sendRedirect("/login");
    }






}
