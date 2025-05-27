package com.leverx.learningmanagementsystem.mailtrap;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.factory.MailtrapClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailTrapConfig {

  @Value("${mailtrap.token}")
  private String token;

  @Bean
  public MailtrapClient mailtrapClient() {
    var config = new io.mailtrap.config.MailtrapConfig.Builder()
        .sandbox(true)
        .inboxId(3711273L)
        .token(token)
        .build();
    return MailtrapClientFactory.createMailtrapClient(config);
  }
}