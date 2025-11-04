package com.microblog.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microblog.models.Notification;
import com.microblog.models.Notification.NotificationType;

public class NotificationView {
  private UUID id;
  private NotificationType type;
  private String actorUsername;
  private String actorId;
  private UUID postId;
  private LocalDateTime createdAt;
  private boolean isRead;

  public NotificationView() {
  }

  public NotificationView(UUID id, NotificationType type, String actorUsername, String actorId,
      UUID postId, LocalDateTime createdAt, boolean isRead) {
    this.id = id;
    this.type = type;
    this.actorUsername = actorUsername;
    this.actorId = actorId;
    this.postId = postId;
    this.createdAt = createdAt;
    this.isRead = isRead;
  }

  public static NotificationView getNotificationView(Notification notification) {

    NotificationView nv = new NotificationView();
    nv.id = notification.getId();
    nv.type = notification.getType();
    nv.actorUsername = notification.getActor().getUsername();
    nv.actorId = notification.getActor().getId();
    nv.postId = notification.getPost() != null ? notification.getPost().getId() : null;
    nv.createdAt = notification.getCreatedAt();
    nv.isRead = notification.isRead();

    return nv;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public NotificationType getType() {
    return type;
  }

  public void setType(NotificationType type) {
    this.type = type;
  }

  public String getActorUsername() {
    return actorUsername;
  }

  public void setActorUsername(String actorUsername) {
    this.actorUsername = actorUsername;
  }

  public String getActorId() {
    return actorId;
  }

  public void setActorId(String actorId) {
    this.actorId = actorId;
  }

  public UUID getPostId() {
    return postId;
  }

  public void setPostId(UUID postId) {
    this.postId = postId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @JsonProperty("isRead")
  public boolean isRead() {
    return isRead;
  }

  public void setRead(boolean read) {
    isRead = read;
  }
}
