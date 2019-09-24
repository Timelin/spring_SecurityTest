package com.magelala.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 发送电子邮件工具类
 * @ClassName:EmailUtil
 * @Author:Timelin
 **/
@Component
@Resource
public class EmailUtil {
    // 日志
    private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    // 发送者
    @Value("${mail.fromMail.sender}")
    private String sender;

    /* 接受者*/
    @Value("${mail.fromMail.receiver}")
    private String receiver;

    @Autowired
    private JavaMailSender javaMailSender;

    // 发送信息到指定邮箱
    public void sendMail(int code, String receiver1) {
        // 发送的内容
        SimpleMailMessage message = new SimpleMailMessage();
        // 发送者
        message.setFrom(sender);
        // 接收者
        message.setTo(receiver);

        logger.info("发送者：{}: 接收者为{} 设置邮箱验证码：{}",sender,receiver,code);
        message.setTo(receiver);
        // 格式
        message.setSubject("小鱼发送文本邮件测试");// 标题
        message.setText("您好！欢迎登录magelala，你的登录验证码是："+code);


        try {
            javaMailSender.send(message);
            logger.info("文件邮件发送成功");

        } catch (MailException e) {
            logger.info("文本邮件发送异常！");

        }
    }


}
