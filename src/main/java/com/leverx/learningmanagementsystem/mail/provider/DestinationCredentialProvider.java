package com.leverx.learningmanagementsystem.mail.provider;

import static com.leverx.learningmanagementsystem.ConstMessages.DESTINATION_NOT_FOUND;
import static com.leverx.learningmanagementsystem.ConstMessages.MISSING_HOST;
import static com.leverx.learningmanagementsystem.ConstMessages.MISSING_PASSWORD;
import static com.leverx.learningmanagementsystem.ConstMessages.MISSING_PORT;
import static com.leverx.learningmanagementsystem.ConstMessages.MISSING_USER;
import static com.leverx.learningmanagementsystem.ConstMessages.SMTP_DESTINATION_NAME;
import static com.leverx.learningmanagementsystem.ConstMessages.SMTP_HOST;
import static com.leverx.learningmanagementsystem.ConstMessages.SMTP_PASSWORD;
import static com.leverx.learningmanagementsystem.ConstMessages.SMTP_PORT;
import static com.leverx.learningmanagementsystem.ConstMessages.SMTP_USER;

import com.leverx.learningmanagementsystem.mail.SmtpCredentialProvider;
import com.leverx.learningmanagementsystem.mail.SmtpObject;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("destinationCredentialProvider")
public class DestinationCredentialProvider implements SmtpCredentialProvider {

  @Override
  public SmtpObject getCredentials() {
    var destination = DestinationAccessor.getLoader()
        .tryGetDestination(SMTP_DESTINATION_NAME)
        .getOrElseThrow(() -> new RuntimeException(DESTINATION_NOT_FOUND));
    var host = (String) destination.get(SMTP_HOST).getOrElseThrow(() -> new RuntimeException(MISSING_HOST));
    var port = Integer.parseInt((String) destination.get(SMTP_PORT).getOrElseThrow(() -> new RuntimeException(MISSING_PORT)));
    var username = (String) destination.get(SMTP_USER).getOrElseThrow(() -> new RuntimeException(MISSING_USER));
    var password = (String) destination.get(SMTP_PASSWORD).getOrElseThrow(() -> new RuntimeException(MISSING_PASSWORD));
    return SmtpObject.builder()
        .host(host)
        .port(port)
        .username(username)
        .password(password)
        .build();
  }
}
