package com.microblog.models;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "follows")
public class Follows implements Serializable {

  @EmbeddedId
  private FollowsId id;

  @MapsId("followerId")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "follower_id", referencedColumnName = "id")
  private User follower;

  @MapsId("followeeId")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "followee_id", referencedColumnName = "id")
  private User followee;

  public Follows() {
  }

  public Follows(User follower, User followee) {
    this.follower = follower;
    this.followee = followee;
    this.id = new FollowsId(follower.getId(), followee.getId());
  }

  public FollowsId getId() {
    return id;
  }

  public void setId(FollowsId id) {
    this.id = id;
  }

  public User getFollower() {
    return follower;
  }

  public void setFollower(User follower) {
    this.follower = follower;
  }

  public User getFollowee() {
    return followee;
  }

  public void setFollowee(User followee) {
    this.followee = followee;
  }
}
