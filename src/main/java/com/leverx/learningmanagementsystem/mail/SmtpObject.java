package com.leverx.learningmanagementsystem.mail;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class SmtpObject {
  private String host;
  private int port;
  private String username;
  private String password;
}
