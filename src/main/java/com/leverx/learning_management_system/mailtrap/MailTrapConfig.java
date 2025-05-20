package com.leverx.learning_management_system.mailtrap;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.factory.MailtrapClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailTrapConfig {

  @Value("${mailtrap.token}")
  private String TOKEN;

  @Bean
  public MailtrapClient mailtrapConfig() {
    var config = new io.mailtrap.config.MailtrapConfig.Builder()
        .sandbox(true)
        .inboxId(3711273L)
        .token(TOKEN)
        .build();
    var client = MailtrapClientFactory.createMailtrapClient(config);
    return client;
  }
}