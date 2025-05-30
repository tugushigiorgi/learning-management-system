package com.leverx.learningmanagementsystem.mail;

import jakarta.mail.MessagingException;

public interface MailService {
  void sendEmail(String[] addresses, String from, String subject, String body) throws MessagingException;
}
