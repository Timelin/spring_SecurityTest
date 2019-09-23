package com.magelala;

import com.magelala.servlet.VerifyServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Servlet;


@SpringBootApplication
@EnableAutoConfiguration
public class SpringbootSecurity01Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootSecurity01Application.class, args);
	}

	/*注入验证码servlet
	* */
	@Bean
	public ServletRegistrationBean indexServletRegistration(){
		// 注入验证码
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new VerifyServlet());
		// 添加获取验证码方式
		registrationBean.addUrlMappings("/getVerifyCode");
		return  registrationBean;

	}
}
