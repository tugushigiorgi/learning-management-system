package com.leverx.learningmanagementsystem.mail.impl;

import static com.leverx.learningmanagementsystem.ConstMessages.UNSUPPORTED_EXCEPTION;

import com.leverx.learningmanagementsystem.mail.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("local")
public class LocalMailServiceImpl implements MailService {
  @Override
  public void sendEmail(String[] addresses, String from, String subject, String body) throws MessagingException {
    throw new UnsupportedOperationException(UNSUPPORTED_EXCEPTION);
  }
}
