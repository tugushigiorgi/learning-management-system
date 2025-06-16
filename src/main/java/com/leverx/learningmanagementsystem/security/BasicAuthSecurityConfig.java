package com.leverx.learningmanagementsystem.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@AllArgsConstructor
@Configuration
@Order(1)
public class BasicAuthSecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    var user = User.withUsername("user")
        .password("password")
        .roles("USER")
        .build();

    var manager = User.withUsername("manager")
        .password("managerpass")
        .roles("MANAGER")
        .build();

    return new InMemoryUserDetailsManager(user, manager);
  }

  @Bean
  @Profile("!prod")
  public SecurityFilterChain localSecurityFilter(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(auth -> auth
         .requestMatchers("/actuator/**").hasRole("MANAGER")
           .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  @Profile("prod")
  public SecurityFilterChain basicAuthFilterChain(HttpSecurity http) throws Exception {
    return http.securityMatcher("/actuator/**")
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/**").hasRole("MANAGER")
            .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }
}
