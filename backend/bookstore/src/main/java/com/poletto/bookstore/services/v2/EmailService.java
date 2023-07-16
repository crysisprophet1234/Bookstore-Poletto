package com.poletto.bookstore.services.v2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import jakarta.mail.MessagingException;
//import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Async
	public void sendEmail(String to, String subject, String body) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);

		mailSender.send(message);

		logger.debug("EMAIL SENT {\"From\": \"" + message.getFrom() + "\","
					+ " \"To\": \"" + message.getTo() + "\","
					+ " \"Subject\": \"" + message.getSubject() + "\","
					+ " \"Body\": \"" + message.getText() + "\"}");

	}

	@Async
	public void sendEmailFromTemplate(String to, String subject, String username) {

		MimeMessage message = mailSender.createMimeMessage();

		try {

			message.setFrom(new InternetAddress("polettobookstore@gmail.com"));
			message.setRecipients(MimeMessage.RecipientType.TO, to);
			message.setSubject(subject);

			String htmlTemplate = generateAccountCreationEmailBody(username);

			message.setContent(htmlTemplate, "text/html; charset=utf-8");

		} catch (MessagingException e) {
			e.printStackTrace();
		}

		mailSender.send(message);

		logger.info("EMAIL SENT {\"From\": \"" + "polettobookstore@gmail.com" + "\","
					+ " \"To\": \"" + to + "\","
					+ " \"Subject\": \"" + subject + "\","
					+ " \"Username\": \"" + username + "\"}");

	}

	private String generateAccountCreationEmailBody(String username) {
		try {
			ClassPathResource resource = new ClassPathResource("templates/account-creation-template.html");
			return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8).replace("{username}",
					username);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
