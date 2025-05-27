package com.leverx.learningmanagementsystem.mailtrap;

import io.mailtrap.model.request.emails.Address;
import java.util.List;

public interface MailTrapService {
  void sendEmail(List<Address> addresses, String from, String subject, String body);
}
