package com.microblog.notifications;

import com.microblog.events.FollowEvent;
import com.microblog.events.LikeEvent;
import com.microblog.events.UnfollowEvent;
import com.microblog.events.UnlikeEvent;

public interface NotificationPusher {

  void pushLikeNotification(LikeEvent likeEvent);

  void pushUnlikeNotification(UnlikeEvent unlikeEvent);

  void pushFollowNotification(FollowEvent followEvent);

  void pushUnfollowNotification(UnfollowEvent unfollowEvent);
}
