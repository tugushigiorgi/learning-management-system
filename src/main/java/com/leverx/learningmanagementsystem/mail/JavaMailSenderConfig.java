package com.leverx.learningmanagementsystem.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import java.util.Properties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;


public class JavaMailSenderConfig {

  public static JavaMailSender javaMailSender(SmtpObject smtp) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(smtp.getHost());
    mailSender.setPort(smtp.getPort());
    mailSender.setUsername(smtp.getUsername());
    mailSender.setPassword(smtp.getPassword());
    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    return mailSender;
  }

  public static void sendSmtpMail(JavaMailSender mailSender, InternetAddress[] addresses, String from, String subject, String body) throws MessagingException {
    var message = mailSender.createMimeMessage();
    var helper = new MimeMessageHelper(message, true);
    helper.setTo(addresses);
    helper.setSubject(subject);
    helper.setText(body);
    helper.setFrom(from);
    mailSender.send(message);
  }
}
