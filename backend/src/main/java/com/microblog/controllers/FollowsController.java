package com.microblog.controllers;

import com.microblog.dto.UserItemView;
import com.microblog.services.FollowsService;
import com.microblog.services.UserMapperService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowsController {

  @Autowired
  private FollowsService followsService;

  @Autowired
  private UserMapperService userMapperService;

  @PostMapping("/{uid}")
  public ResponseEntity<Void> follow(@PathVariable String uid) {
    followsService.followUser(uid);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{uid}")
  public ResponseEntity<Void> unfollow(@PathVariable String uid) {
    followsService.unfollowUser(uid);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/followers/{id}")
  public List<UserItemView> getFollowers(@PathVariable String id) {
    return userMapperService.toUserItemViewList(followsService.getFollowers(id));
  }

  @GetMapping("/followees/{id}")
  public List<UserItemView> getFollowees(@PathVariable String id) {
    return userMapperService.toUserItemViewList(followsService.getFollowees(id));
  }
}
