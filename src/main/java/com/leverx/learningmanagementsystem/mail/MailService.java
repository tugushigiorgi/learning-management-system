package com.leverx.learningmanagementsystem.mail;

import io.mailtrap.model.request.emails.Address;
import java.util.List;

public interface MailService {
  void sendEmail(List<Address> addresses, String from, String subject, String body);
}
