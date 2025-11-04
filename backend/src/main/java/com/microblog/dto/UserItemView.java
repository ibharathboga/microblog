package com.microblog.dto;

public class UserItemView extends UserItem {
  private Boolean isFollowed;

  public Boolean getIsFollowed() {
    return isFollowed;
  }

  public void setIsFollowed(Boolean isFollowed) {
    this.isFollowed = isFollowed;
  }
}
