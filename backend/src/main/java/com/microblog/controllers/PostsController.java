package com.microblog.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuthException;
import com.microblog.dto.PostView;
import com.microblog.models.Post;
import com.microblog.services.PostMapperService;
import com.microblog.services.PostService;

@RestController
@RequestMapping("/posts")
public class PostsController {

  @Autowired
  private PostService postService;

  @Autowired
  private PostMapperService postMapperService;

  @GetMapping("/{username}")
  public List<PostView> getAllPostsByUsername(@PathVariable String username)
      throws FirebaseAuthException {
    List<Post> posts = postService.getPostsByUsername(username);
    return postMapperService.toPostViewList(posts);
  }

  @PostMapping
  public PostView addPost(@RequestBody Post post) throws FirebaseAuthException {
    return postMapperService.toPostView(postService.addPost(post), false, 0);
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable UUID postId) {
    postService.deletePost(postId);
    return ResponseEntity.noContent().build();
  }
}
