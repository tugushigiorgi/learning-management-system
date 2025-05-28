package com.leverx.learningmanagementsystem;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "sap.ffs")
public class FfsConfig {
  private String uri;
  private String username;
  private String password;
}
