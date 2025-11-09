package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public void sendOtpMail(String toEmail,String subject,String otp){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("maga986456@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText("Salam,\n\nSizin təsdiqləmə kodunuz: " + otp + "\n\nKod 3 dəqiqə ərzində etibarlıdır.");
        javaMailSender.send(message);
    }
}
