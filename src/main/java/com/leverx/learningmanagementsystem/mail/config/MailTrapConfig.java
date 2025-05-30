package com.leverx.learningmanagementsystem.mail.config;

import com.leverx.learningmanagementsystem.mail.properties.MailTrapProperties;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.factory.MailtrapClientFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MailTrapConfig {

  private final MailTrapProperties properties;

  @Bean
  public MailtrapClient mailtrapClient() {
    var config = new io.mailtrap.config.MailtrapConfig.Builder()
        .sandbox(true)
        .inboxId(properties.getId())
        .token(properties.getToken())
        .build();
    return MailtrapClientFactory.createMailtrapClient(config);
  }
}