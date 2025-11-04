package com.microblog.events;

import com.microblog.models.Like;

public class LikeEvent {
  private final Like like;

  public LikeEvent(Like like) {
    this.like = like;
  }

  public Like getLike() {
    return like;
  }
}
