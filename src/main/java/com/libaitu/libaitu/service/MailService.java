package com.libaitu.libaitu.service;

import com.libaitu.libaitu.exception.EmailSenderException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class MailService {

    @Autowired()
    private JavaMailSender mailSender;



    public void send(String to,String emailBody,String subject) {
        try {
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,"utf-8");
            helper.setText(emailBody);
            helper.setFrom("ertaev.nurxan@bk.ru");
            helper.setTo(to);
            helper.setSubject(subject);
            mailSender.send(mimeMessage);
        }
        catch (MessagingException e){
            throw new EmailSenderException("failed to send email");
        }



    }
}
