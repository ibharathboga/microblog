package com.microblog.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class LikeId implements Serializable {
  private String userId;
  private UUID postId;

  public LikeId() {
  }

  public LikeId(String userId, UUID postId) {
    this.userId = userId;
    this.postId = postId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public UUID getPostId() {
    return postId;
  }

  public void setPostId(UUID postId) {
    this.postId = postId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof LikeId))
      return false;
    LikeId that = (LikeId) o;
    return Objects.equals(userId, that.userId) && Objects.equals(postId, that.postId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, postId);
  }
}
