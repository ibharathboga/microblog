package com.microblog.dto;

public class UserProfileView extends UserProfile {
  private Boolean isFollowed;

  public Boolean getIsFollowed() {
    return isFollowed;
  }

  public void setIsFollowed(Boolean isFollowed) {
    this.isFollowed = isFollowed;
  }

}
