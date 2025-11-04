package com.microblog.events;

import com.microblog.models.Like;

public class UnlikeEvent {
  private final Like like;

  public UnlikeEvent(Like like) {
    this.like = like;
  }

  public Like getLike() {
    return like;
  }
}