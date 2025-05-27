package com.leverx.learningmanagementsystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MainController {

  @GetMapping("/")
  public String hello() {
    log.info("Entered into index page");
    return "Hello World!";
  }
}
