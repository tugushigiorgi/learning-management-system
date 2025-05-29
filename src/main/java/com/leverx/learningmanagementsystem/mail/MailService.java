package com.leverx.learningmanagementsystem.mail;

import io.mailtrap.model.request.emails.Address;
import jakarta.mail.MessagingException;
import java.util.List;

public interface MailService<T> {
  void sendEmail(T[] addresses, String from, String subject, String body) throws MessagingException;
}
