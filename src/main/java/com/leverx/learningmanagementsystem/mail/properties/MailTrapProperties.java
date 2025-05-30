package com.leverx.learningmanagementsystem.mail.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "mailtrap")
public class MailTrapProperties {
  private String token;
  private long id;
}
