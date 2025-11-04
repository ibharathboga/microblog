package com.microblog.events;

import com.microblog.models.Follows;

public class UnfollowEvent {
  private final Follows follows;

  public UnfollowEvent(Follows follows) {
    this.follows = follows;
  }

  public Follows getFollows() {
    return follows;
  }
}
