package com.leverx.learning_management_system.mailtrap;

import io.mailtrap.model.request.emails.Address;
import java.util.List;

public interface MailTrapService {
  void sendEmail(List<Address> addresses, String from, String subject, String body);
}
