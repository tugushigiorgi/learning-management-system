package com.leverx.learningmanagementsystem.mail.impl;

import com.leverx.learningmanagementsystem.mail.MailService;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class MailTrapServiceImpl implements MailService {

  private final MailtrapClient mailtrapClient;

  @Override
  public void sendEmail(String[] addresses, String from, String subject, String body) {
    var mail = MailtrapMail.builder()
        .from(new Address(from))
        .to(Arrays.stream(addresses)
            .map(Address::new)
            .toList())
        .subject(subject)
        .text(body)
        .build();
    try {
      System.out.println(mailtrapClient.send(mail));
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
