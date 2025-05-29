package com.leverx.learningmanagementsystem.mail;

import jakarta.mail.MessagingException;

public interface MailService<T> {
  void sendEmail(T[] addresses, String from, String subject, String body) throws MessagingException;
}
