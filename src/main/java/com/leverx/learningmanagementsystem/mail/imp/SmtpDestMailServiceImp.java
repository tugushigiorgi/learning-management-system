package com.leverx.learningmanagementsystem.mail.imp;

import static com.leverx.learningmanagementsystem.ConstMessages.SMTP_DESTINATION_NAME;

import com.leverx.learningmanagementsystem.mail.JavaMailSenderConfig;
import com.leverx.learningmanagementsystem.mail.MailService;
import com.leverx.learningmanagementsystem.mail.SmtpObject;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SmtpDestMailServiceImp implements MailService<InternetAddress> {

  private SmtpObject getCredentialsFromDestination() {
    var destination = DestinationAccessor.getLoader()
        .tryGetDestination(SMTP_DESTINATION_NAME)
        .getOrElseThrow(() -> new RuntimeException("Destination not found"));
    var host = (String) destination.get("mail.smtp.host").getOrElseThrow(() -> new RuntimeException("Missing host"));
    var port = Integer.parseInt((String) destination.get("mail.smtp.port").getOrElseThrow(() -> new RuntimeException("Missing port")));
    var username = (String) destination.get("mail.user").getOrElseThrow(() -> new RuntimeException("Missing email"));
    var password = (String) destination.get("mail.password").getOrElseThrow(() -> new RuntimeException("Missing password"));
    return SmtpObject.builder()
        .host(host)
        .port(port)
        .username(username)
        .password(password)
        .build();
  }

  @Override
  public void sendEmail(InternetAddress[] addresses, String from, String subject, String body) throws MessagingException {
    var mailSender = JavaMailSenderConfig.javaMailSender(getCredentialsFromDestination());
    JavaMailSenderConfig.sendSmtpMail(mailSender, addresses, from, subject, body);
  }
}
