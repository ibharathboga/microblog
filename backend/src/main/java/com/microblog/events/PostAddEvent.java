package com.microblog.events;

import com.microblog.models.Post;

public class PostAddEvent {
  private final Post post;

  public PostAddEvent(Post post) {
    this.post = post;
  }

  public Post getPost() {
    return post;
  }
}
