package com.microblog.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microblog.events.AppEventPublisher;
import com.microblog.events.LikeEvent;
import com.microblog.events.UnlikeEvent;
import com.microblog.models.Like;
import com.microblog.models.Post;
import com.microblog.models.User;
import com.microblog.repositories.LikeRepository;
import com.microblog.repositories.PostRepository;
import com.microblog.repositories.UserRepository;

@Service
public class LikeService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private LikeRepository likeRepository;

  @Autowired
  private CurrentUserService currentUser;

  @Autowired
  private AppEventPublisher appEventPublisher;

  public Like likePost(UUID postId) {
    User user = userRepository.findById(currentUser.getId()).orElseThrow();
    Post post = postRepository.findById(postId).orElseThrow();
    if (!likeRepository.findByUserAndPost(user, post).isEmpty()) {
      throw new RuntimeException("Already liked");
    }
    Like like = new Like(user, post);
    appEventPublisher.publish(new LikeEvent(like));
    return likeRepository.save(like);
  }

  public void unlikePost(UUID postId) {
    User user = userRepository.findById(currentUser.getId()).orElseThrow();
    Post post = postRepository.findById(postId).orElseThrow();
    Like unlike = new Like(user, post);
    appEventPublisher.publish(new UnlikeEvent(unlike));
    likeRepository.deleteByUserAndPost(user, post);
  }

  public List<Post> getLikedPostsByUsername(String username) {
    User user = userRepository.findByUsername(username).orElseThrow();
    return user.getLikes().stream().map(Like::getPost).collect(Collectors.toList());
  }

  public List<User> getUsersWhoLikedPost(UUID postId) {
    Post post = postRepository.findById(postId).orElseThrow();
    return likeRepository.findByPost(post).stream().map(Like::getUser).toList();
  }
}
