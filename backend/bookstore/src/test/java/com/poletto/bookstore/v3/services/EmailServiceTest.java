package com.poletto.bookstore.v3.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.services.v2.EmailService;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @SpyBean
    private JavaMailSender mailSender;

    @InjectMocks
    @Autowired
    private EmailService emailService;

    @Test
    public void testSendEmail() {

        String to = "polettobookstore@gmail.com";
        String subject = "Test Subject";
        String body = "Test Body, right now is: " + new Date();

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(to);
        expectedMessage.setSubject(subject);
        expectedMessage.setText(body);

        emailService.sendEmail(to, subject, body);
        verify(mailSender, times(1)).send(expectedMessage);
        
    }
        
}

