package com.microblog.events;

import com.microblog.models.Follows;

public class FollowEvent {
  private final Follows follows;

  public FollowEvent(Follows follows) {
    this.follows = follows;
  }

  public Follows getFollows() {
    return follows;
  }
}
