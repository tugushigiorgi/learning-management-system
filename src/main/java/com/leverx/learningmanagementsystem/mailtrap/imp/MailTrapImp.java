package com.leverx.learningmanagementsystem.mailtrap.imp;

import com.leverx.learningmanagementsystem.mailtrap.MailTrapService;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailTrapImp implements MailTrapService {

  private final MailtrapClient mailtrapClient;

  @Override
  public void sendEmail(List<Address> addresses, String from, String subject, String body) {
    var mail = MailtrapMail.builder()
        .from(new Address(from))
        .to(addresses)
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
