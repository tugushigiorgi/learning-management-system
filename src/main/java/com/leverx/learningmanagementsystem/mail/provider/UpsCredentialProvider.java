package com.leverx.learningmanagementsystem.mail.provider;

import com.leverx.learningmanagementsystem.SmtpUpsConfig;
import com.leverx.learningmanagementsystem.mail.SmtpCredentialProvider;
import com.leverx.learningmanagementsystem.mail.SmtpObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("upsCredentialProvider")
public class UpsCredentialProvider implements SmtpCredentialProvider {
  private final SmtpUpsConfig smtpUpsConfig;

  @Override
  public SmtpObject getCredentials() {
    return SmtpObject.builder()
        .host(smtpUpsConfig.getHost())
        .port(smtpUpsConfig.getPort())
        .username(smtpUpsConfig.getUsername())
        .password(smtpUpsConfig.getPassword())
        .build();
  }
}
