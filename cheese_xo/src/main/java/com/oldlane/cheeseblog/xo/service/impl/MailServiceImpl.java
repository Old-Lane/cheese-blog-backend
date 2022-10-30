package com.oldlane.cheeseblog.xo.service.impl;

import com.oldlane.cheeseblog.xo.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Date: 2022/10/9 21:46
 * Description:
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;//一定要用@Autowired

    //application.properties中已配置的值
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendMimeMail(String email,String comment, String nickname) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("孑栖评论回复");//主题

            mailMessage.setText("您收到来自孑栖用户『" + nickname + "』的评论回复：" + comment);//内容

            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom(from);//你自己的邮箱

            mailSender.send(mailMessage);//发送
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("孑栖用户留言");//主题
            mailMessage.setText("有人给我留言："+ message);//内容
            mailMessage.setTo("1587664551@qq.com");//发给谁
            mailMessage.setFrom(from);//你自己的邮箱
            mailSender.send(mailMessage);//发送
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendValidateCode(String validateCode, String email, String theme) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject(theme);//主题

            mailMessage.setText("您收到的验证码："+ validateCode);//内容

            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom(from);//你自己的邮箱

            mailSender.send(mailMessage);//发送
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

