package com.microblog.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FollowsId implements Serializable {
  private String followerId;
  private String followeeId;

  public FollowsId() {
  }

  public FollowsId(String followerId, String followeeId) {
    this.followerId = followerId;
    this.followeeId = followeeId;
  }

  public String getFollowerId() {
    return followerId;
  }

  public void setFollowerId(String followerId) {
    this.followerId = followerId;
  }

  public String getFolloweeId() {
    return followeeId;
  }

  public void setFolloweeId(String followeeId) {
    this.followeeId = followeeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof FollowsId))
      return false;
    FollowsId that = (FollowsId) o;
    return Objects.equals(followerId, that.followerId) &&
        Objects.equals(followeeId, that.followeeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(followerId, followeeId);
  }
}
