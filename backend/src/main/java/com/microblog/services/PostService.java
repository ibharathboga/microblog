package com.microblog.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microblog.events.AppEventPublisher;
import com.microblog.events.PostAddEvent;
import com.microblog.models.Post;
import com.microblog.repositories.PostRepository;

@Service
public class PostService {

  @Autowired
  private AppEventPublisher appEventPublisher;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private CurrentUserService currentUser;

  public List<Post> getPostsByUsername(String username) {
    return postRepository.findAllByAuthorUsername(username);
  }

  public Post addPost(Post post) {
    post.setAuthor(currentUser.getUser());
    Post savedPost = postRepository.save(post);
    appEventPublisher.publish(new PostAddEvent(savedPost));
    return savedPost;
  }

  public void deletePost(UUID postId) {
    postRepository.deleteById(postId);
  }
}
