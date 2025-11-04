package com.microblog.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microblog.events.AppEventPublisher;
import com.microblog.events.FollowEvent;
import com.microblog.events.UnfollowEvent;
import com.microblog.models.Follows;
import com.microblog.models.FollowsId;
import com.microblog.models.User;
import com.microblog.repositories.FollowsRepository;
import com.microblog.repositories.UserRepository;

@Service
public class FollowsService {

  @Autowired
  private AppEventPublisher appEventPublisher;

  @Autowired
  private FollowsRepository followsRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CurrentUserService currentUser;

  public void followUser(String targetUserId) {
    if (followsRepository.existsByFollower_IdAndFollowee_Id(currentUser.getId(), targetUserId)) {
      return;
    }
    User follower = userRepository.findById(currentUser.getId()).orElseThrow();
    User followee = userRepository.findById(targetUserId).orElseThrow();
    Follows follow = new Follows(follower, followee);
    followsRepository.save(follow);
    appEventPublisher.publish(new FollowEvent(follow));
  }

  public void unfollowUser(String targetUserId) {
    FollowsId unfollowsId = new FollowsId(currentUser.getId(), targetUserId);

    Follows unfollow = followsRepository.findById(unfollowsId).orElseThrow();
    followsRepository.deleteById(unfollowsId);
    appEventPublisher.publish(new UnfollowEvent(unfollow));
  }

  public List<User> getFollowers(String targetUserId) {
    List<Follows> follows = followsRepository.findByFollowee_Id(targetUserId);
    return follows
        .stream()
        .map(f -> f.getFollower())
        .collect(Collectors.toList());
  }

  public List<User> getFollowees(String targetUserId) {
    List<Follows> follows = followsRepository.findByFollower_Id(targetUserId);
    return follows
        .stream()
        .map(f -> f.getFollowee())
        .collect(Collectors.toList());
  }

  // from here on for userMapperService
  public Boolean isFollowing(String sourceUserId, String targetUserId) {
    if (sourceUserId.equals(targetUserId)) {
      return null;
    }
    return followsRepository.existsByFollower_IdAndFollowee_Id(sourceUserId, targetUserId);
  }

  public Set<String> getFollowedUserIds(String sourceUserId, List<String> targetUserIds) {
    return followsRepository.findFollowedUserIds(sourceUserId, targetUserIds);
  }

}
