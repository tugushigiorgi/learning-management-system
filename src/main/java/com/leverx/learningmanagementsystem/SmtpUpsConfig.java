package com.leverx.learningmanagementsystem;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "smtp-ups")
public class SmtpUpsConfig {
  private String host;
  private int port;
  private String username;
  private String password;
}
