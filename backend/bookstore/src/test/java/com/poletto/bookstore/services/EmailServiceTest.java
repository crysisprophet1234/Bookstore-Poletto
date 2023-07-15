package com.poletto.bookstore.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.poletto.bookstore.services.v2.EmailService;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceTest.class);

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendEmail() {
        // Prepare test data
        String to = "ultimateleodib@gmail.com";
        String subject = "Test Subject";
        String body = "Test Body Right now is: " + new Date();

        // Define expected behavior
        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(to);
        expectedMessage.setSubject(subject);
        expectedMessage.setText(body);

        // Mock the mail sender and verify the message is sent
        emailService.sendEmail(to, subject, body);
        verify(mailSender, times(1)).send(expectedMessage);
        
        logger.info("EMAIL SENT {\"From\": \"" 		+ expectedMessage.getFrom() 	+
        			"\", \"To\": \"" 		+ expectedMessage.getTo() 		+
        			"\", \"Subject\": \"" 	+ expectedMessage.getSubject()	+
        			"\", \"Body\": \"" 		+ expectedMessage.getText()		+ "\"}");
        
    }
}

