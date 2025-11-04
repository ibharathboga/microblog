package com.microblog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.firebase.auth.FirebaseAuthException;
import com.microblog.dto.UserInfo;
import com.microblog.services.FollowsService;
import com.microblog.services.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FeedServiceTest extends BaseSecurityTest {

  @Autowired
  private FollowsService followsService;

  @Autowired
  private UserService userService;

  @Test
  void followTest() throws FirebaseAuthException {
    String username = "demouser";
    UserInfo u = userService.getUserInfoByUsername(username);
    followsService.followUser(u.getId());
  }

  @Test
  void unfollowTest() throws FirebaseAuthException {
    String username = "demouser";
    UserInfo u = userService.getUserInfoByUsername(username);
    followsService.unfollowUser(u.getId());
  }

}
