package com.leverx.learningmanagementsystem;

import com.sap.cloud.security.xsuaa.token.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {


  @GetMapping("/api/main")
  public ResponseEntity<String> readAll(@AuthenticationPrincipal Token token) {

    var lis=token.getAuthorities().toArray();
    String d="";
    for (int i = 0; i < lis.length; i++) {
      d+=lis[i].toString();
    }
    return new ResponseEntity<String>(d, HttpStatus.OK);
  }
}
