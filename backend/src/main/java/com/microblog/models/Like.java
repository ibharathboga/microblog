package com.microblog.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "likes")
public class Like {

  @EmbeddedId
  private LikeId id;

  @MapsId("userId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonBackReference(value = "user-likes")
  private User user;

  @MapsId("postId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "post_id", nullable = false)
  @JsonBackReference(value = "post-likes")
  private Post post;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime likedAt;

  public Like() {
  }

  public Like(User user, Post post) {
    this.user = user;
    this.post = post;
    this.id = new LikeId(user.getId(), post.getId());
  }

  public LikeId getId() {
    return id;
  }

  public void setId(LikeId id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public LocalDateTime getLikedAt() {
    return likedAt;
  }

  public void setLikedAt(LocalDateTime likedAt) {
    this.likedAt = likedAt;
  }
}
