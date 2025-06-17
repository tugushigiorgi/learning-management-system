package com.leverx.learningmanagementsystem.mail.provider;

import com.leverx.learningmanagementsystem.mail.SmtpCredentialProvider;
import com.leverx.learningmanagementsystem.mail.SmtpObject;
import com.leverx.learningmanagementsystem.mail.properties.SmtpUserProvidedProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("userProvidedCredentialProvider")
public class UserProvidedCredentialProvider implements SmtpCredentialProvider {
  private final SmtpUserProvidedProperties smtpUserProvidedProperties;

  @Override
  public SmtpObject getCredentials() {
    return SmtpObject.builder()
        .host(smtpUserProvidedProperties.getHost())
        .port(smtpUserProvidedProperties.getPort())
        .username(smtpUserProvidedProperties.getUsername())
        .password(smtpUserProvidedProperties.getPassword())
        .build();
  }
}
