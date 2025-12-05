package com.unin.airlines.user_service.service.impl;

import com.unin.airlines.user_service.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender mailSender;


    @Override
    @Async
    public void sendSimpleMail(String receiverEmail, String sub, String message) {

        SimpleMailMessage msg = new SimpleMailMessage();
        try {
            log.info("creating bmail body for {}  to {}",sub, receiverEmail);
            msg.setFrom(sender);
            msg.setTo(receiverEmail);
            msg.setSubject(sub);
            msg.setText(message);
            log.info("sending mail to {} for {}",receiverEmail,sub);
            mailSender.send(msg);
            log.info("Mail Sent successfully to {} for {}}", receiverEmail, sub);
        }
        catch (Exception ex){
            log.error("sending mail failed");
            throw new RuntimeException("User creation failed: " + ex.getMessage());
//            return false;
        }
//        return null;
    }
}
