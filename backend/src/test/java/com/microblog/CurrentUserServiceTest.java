package com.microblog;

import com.google.firebase.auth.FirebaseAuthException;
import com.microblog.dto.UserInfo;
import com.microblog.services.CurrentUserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrentUserServiceTest extends BaseSecurityTest {

  @Autowired
  private CurrentUserService currentUser;

  @Test
  void authMeReturnsCurrentUserInfo() throws FirebaseAuthException {
    UserInfo userInfo = currentUser.getInfo();
    assertEquals("jake@gmail.com", userInfo.getEmail());
    assertEquals("jake", userInfo.getUsername());
  }
}
