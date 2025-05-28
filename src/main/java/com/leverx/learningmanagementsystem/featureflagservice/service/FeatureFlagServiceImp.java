package com.leverx.learningmanagementsystem.featureflagservice.service;

import static com.leverx.learningmanagementsystem.ConstMessages.EVALUATE_URI_FORMAT;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import com.leverx.learningmanagementsystem.FfsConfig;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@Slf4j
@RequiredArgsConstructor
public class FeatureFlagServiceImp implements FeatureFlagService {

  private final FfsConfig ffsConfig;

  @Override
  public boolean isFeatureEnabled(String feature) throws IOException {
    var evaluationUri = EVALUATE_URI_FORMAT.formatted(ffsConfig.getUri(), feature);
    var credentials = encodeBase64String((ffsConfig.getUsername() + ":" + ffsConfig.getPassword()).getBytes());
    var httpClient = HttpClientBuilder.create().build();
    var evaluateRequest = new HttpGet(evaluationUri);
    evaluateRequest.addHeader("Authorization", "Basic " + credentials);
    var response = httpClient.execute(evaluateRequest);
    var statusCode = response.getStatusLine().getStatusCode();
    return switch (statusCode) {
      case 200 -> {
        log.warn("{} is active", feature);
        yield true;
      }
      case 204 -> {
        log.warn("{} is disabled", feature);
        yield false;
      }
      case 404 -> {
        log.warn("{} is missing", feature);
        yield false;
      }
      case 400 -> {
        log.warn("flag input error");
        yield false;
      }
      default -> {
        log.warn("flag is inactive");
        yield false;
      }
    };
  }
}
