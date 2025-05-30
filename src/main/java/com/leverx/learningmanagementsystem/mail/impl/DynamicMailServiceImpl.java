package com.leverx.learningmanagementsystem.mail.impl;

import static com.leverx.learningmanagementsystem.ConstMessages.FLAG_CHECK_FAILED;
import static com.leverx.learningmanagementsystem.ConstMessages.SMTP_FLAG;

import com.leverx.learningmanagementsystem.featureflagservice.FeatureFlagService;
import com.leverx.learningmanagementsystem.mail.MailService;
import com.leverx.learningmanagementsystem.mail.SmtpCredentialProvider;
import com.leverx.learningmanagementsystem.mail.util.JavaMailSenderUtil;
import jakarta.mail.MessagingException;
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
public class DynamicMailServiceImpl implements MailService {

  private final FeatureFlagService featureFlagService;
  private final SmtpCredentialProvider userProvidedCredentialProvider;
  private final SmtpCredentialProvider destinationCredentialProvider;

  @Override
  public void sendEmail(String[] addresses, String from, String subject, String body) throws MessagingException {
    boolean useDestination = false;
    try {
      useDestination = featureFlagService.isFeatureEnabled(SMTP_FLAG);
    } catch (IOException e) {
      log.warn(FLAG_CHECK_FAILED, e);
    }
    var provider = useDestination ? destinationCredentialProvider : userProvidedCredentialProvider;
    var smtp = provider.getCredentials();
    var mailSender = JavaMailSenderUtil.getJavaMailSender(smtp);
    JavaMailSenderUtil.sendSmtpMail(mailSender, addresses, from, subject, body);
  }
}
