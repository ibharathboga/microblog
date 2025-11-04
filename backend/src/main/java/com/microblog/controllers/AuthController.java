package com.microblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.microblog.dto.UserInfo;
import com.microblog.services.AuthService;
import com.microblog.services.CurrentUserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private CurrentUserService currentUser;

  public record RegisterRequest(String username, String email, String password) {
  }

  public record LoginRequest(String idToken) {
  }

  @GetMapping("/me")
  public UserInfo me(@AuthenticationPrincipal FirebaseToken principal) throws FirebaseAuthException {
    return currentUser.getInfo();
  }

  @PostMapping("/register")
  public UserInfo register(@RequestBody RegisterRequest request) throws FirebaseAuthException {
    return authService.register(request.username(), request.email(), request.password());
  }

}
