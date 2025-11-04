package com.microblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.microblog.dto.UserInfo;
import com.microblog.models.User;
import com.microblog.repositories.UserRepository;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FirebaseAuth firebaseAuth;

  public UserInfo register(String username, String email, String password)
      throws IllegalArgumentException, FirebaseAuthException {

    if (userRepository.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("Username is already taken");
    }

    UserRecord.CreateRequest request = new UserRecord.CreateRequest()
        .setEmail(email)
        .setPassword(password);
    UserRecord userRecord = firebaseAuth.createUser(request);

    User user = new User();
    user.setId(userRecord.getUid());
    user.setUsername(username);
    userRepository.save(user);

    UserInfo userInfo = new UserInfo();
    userInfo.setId(userRecord.getUid());
    userInfo.setEmail(userRecord.getEmail());
    userInfo.setUsername(username);
    userInfo.setCreationTimestamp(userRecord.getUserMetadata().getCreationTimestamp());
    return userInfo;
  }

  public void deleteUser(String uid) throws FirebaseAuthException {
    firebaseAuth.deleteUser(uid);
  }
}
