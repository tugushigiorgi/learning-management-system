package com.leverx.learningmanagementsystem.mail;

import static com.leverx.learningmanagementsystem.ConstMessages.FLAG_CHECK_FAILED;
import static com.leverx.learningmanagementsystem.ConstMessages.SMTP_FLAG;

import com.leverx.learningmanagementsystem.featureflagservice.service.FeatureFlagService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
@Profile("prod")
public class DynamicMailService implements MailService<InternetAddress> {

  private final FeatureFlagService featureFlagService;
  private final SmtpCredentialProvider upsCredentialProvider;
  private final SmtpCredentialProvider destinationCredentialProvider;

  @Override
  public void sendEmail(InternetAddress[] addresses, String from, String subject, String body) throws MessagingException {
    boolean useDestination = false;
    try {
      useDestination = featureFlagService.isFeatureEnabled(SMTP_FLAG);
    } catch (IOException e) {
      log.warn(FLAG_CHECK_FAILED, e);
    }
    var provider = useDestination ? destinationCredentialProvider : upsCredentialProvider;
    var smtp = provider.getCredentials();
    var mailSender = JavaMailSenderUtil.javaMailSender(smtp);
    JavaMailSenderUtil.sendSmtpMail(mailSender, addresses, from, subject, body);
  }
}
