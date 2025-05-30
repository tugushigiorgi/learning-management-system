package com.leverx.learningmanagementsystem.mail.config;

import com.leverx.learningmanagementsystem.mail.properties.MailTrapProperties;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.factory.MailtrapClientFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class MailTrapConfig {

  private MailTrapProperties properties;

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