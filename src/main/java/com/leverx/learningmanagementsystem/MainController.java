package com.leverx.learningmanagementsystem;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
@Profile("prod")
public class MainController {
  @GetMapping("/")
  public
  String hello() throws IOException {
    return "Hello World";
  }
}
