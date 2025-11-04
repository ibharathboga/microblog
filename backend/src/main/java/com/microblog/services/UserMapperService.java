package com.microblog.services;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microblog.dto.UserInfo;
import com.microblog.dto.UserItemView;
import com.microblog.dto.UserProfileView;
import com.microblog.models.User;

@Service
public class UserMapperService {

  @Autowired
  private FollowsService followService;

  @Autowired
  private CurrentUserService currentUser;

  public UserProfileView toUserProfileView(UserInfo userInfo) {
    UserProfileView view = new UserProfileView();
    view.setId(userInfo.getId());
    view.setUsername(userInfo.getUsername());
    view.setCreatedAt(Instant.ofEpochMilli(userInfo.getCreationTimestamp()).toString());
    view.setIsFollowed(followService.isFollowing(currentUser.getId(), userInfo.getId()));
    return view;
  }

  private UserItemView toUserItemView(User user, Boolean isFollowing) {
    UserItemView view = new UserItemView();
    view.setId(user.getId());
    view.setUsername(user.getUsername());
    view.setIsFollowed(isFollowing);
    return view;
  }

  public List<UserItemView> toUserItemViewList(List<User> users) {
    List<String> targetUserIds = users.stream().map(User::getId).toList();
    Set<String> followedUserIds = followService.getFollowedUserIds(currentUser.getId(), targetUserIds);
    return users
        .stream()
        .map(user -> {
          Boolean isFollowing = followedUserIds.contains(user.getId());
          isFollowing = user.getId().equals(currentUser.getId()) ? null : isFollowing;
          return toUserItemView(user, isFollowing);
        })
        .toList();
  }

}
