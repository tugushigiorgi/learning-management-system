package com.leverx.learningmanagementsystem.featureflagservice.impl;

import static com.leverx.learningmanagementsystem.ConstMessages.EVALUATE_URI_FORMAT;
import static com.leverx.learningmanagementsystem.ConstMessages.FLAG_INPUT_ERROR;
import static com.leverx.learningmanagementsystem.ConstMessages.FLAG_IS_INACTIVE;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import com.leverx.learningmanagementsystem.featureflagservice.FeatureFlagProperties;
import com.leverx.learningmanagementsystem.featureflagservice.FeatureFlagService;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeatureFlagServiceImpl implements FeatureFlagService {

  private final FeatureFlagProperties featureFlagProperties;
  private final CloseableHttpClient httpClient;
  private static final Map<Integer, Boolean> STATUS_FLAG_MAP = Map.of(
      200, true,
      204, false,
      404, false,
      400, false
  );

  @Override
  public boolean isFeatureEnabled(String feature) throws IOException {
    var uri = EVALUATE_URI_FORMAT.formatted(featureFlagProperties.getUri(), feature);
    var credentials = encodeBase64String((featureFlagProperties.getUsername() + ":" + featureFlagProperties.getPassword()).getBytes());
    var request = new HttpGet(uri);
    request.addHeader("Authorization", "Basic " + credentials);
    try (var response = httpClient.execute(request)) {
      var status = response.getStatusLine().getStatusCode();
      logStatus(feature, status);
      return STATUS_FLAG_MAP.getOrDefault(status, false);
    }
  }

  private void logStatus(String feature, int status) {
    switch (status) {
      case 200 -> log.warn("{} is active", feature);
      case 204 -> log.warn("{} is disabled", feature);
      case 404 -> log.warn("{} is missing", feature);
      case 400 -> log.warn(FLAG_INPUT_ERROR);
      default -> log.warn(FLAG_IS_INACTIVE);
    }
  }
}
