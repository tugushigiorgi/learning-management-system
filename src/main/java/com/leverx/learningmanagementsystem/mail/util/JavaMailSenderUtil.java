package com.leverx.learningmanagementsystem.mail.util;

import com.leverx.learningmanagementsystem.mail.SmtpObject;
import jakarta.mail.MessagingException;
import java.util.Properties;
import lombok.experimental.UtilityClass;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

@UtilityClass
public class JavaMailSenderUtil {

  public static JavaMailSender getJavaMailSender(SmtpObject smtp) {
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

  public static void sendSmtpMail(JavaMailSender mailSender, String[] addresses, String from, String subject, String body) throws MessagingException {
    var message = mailSender.createMimeMessage();
    var helper = new MimeMessageHelper(message, true);
    helper.setTo(addresses);
    helper.setSubject(subject);
    helper.setText(body);
    helper.setFrom(from);
    mailSender.send(message);
  }
}