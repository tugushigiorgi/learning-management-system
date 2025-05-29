package com.leverx.learningmanagementsystem.mail.imp;

import com.leverx.learningmanagementsystem.mail.MailService;
import jakarta.mail.internet.InternetAddress;
import org.springframework.stereotype.Service;

@Service
public class smtpUpsServiceImp  implements MailService<InternetAddress> {










  @Override
  public void sendEmail(InternetAddress[] addresses, String from, String subject, String body) {

  }
}
