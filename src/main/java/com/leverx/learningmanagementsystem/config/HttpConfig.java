package com.leverx.learningmanagementsystem.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {

  @Bean
  public CloseableHttpClient httpClient() {
    return HttpClientBuilder.create().build();
  }
}
