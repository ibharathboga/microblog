package com.microblog.services;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.microblog.dto.UserInfo;
import com.microblog.models.User;
import com.microblog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  private FirebaseToken getCurrentUserToken() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Object principal = auth.getPrincipal();
    if (principal instanceof FirebaseToken) {
      return (FirebaseToken) principal;
    }
    return null;
  }

  public User getUser() {
    FirebaseToken token = getCurrentUserToken();
    return userRepository.findById(token.getUid()).orElseThrow();
  }

  public UserInfo getInfo() throws FirebaseAuthException {
    FirebaseToken token = getCurrentUserToken();
    User user = userRepository.findById(token.getUid()).orElseThrow();
    return userService.getUserInfoByUsername(user.getUsername());
  }

  public String getId() {
    FirebaseToken token = getCurrentUserToken();
    return (token != null) ? token.getUid() : null;
  }

}
