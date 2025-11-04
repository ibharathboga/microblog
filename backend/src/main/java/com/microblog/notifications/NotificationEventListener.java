package com.microblog.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.microblog.dto.NotificationView;
import com.microblog.events.FollowEvent;
import com.microblog.events.LikeEvent;
import com.microblog.events.UnfollowEvent;
import com.microblog.events.UnlikeEvent;
import com.microblog.models.Notification;
import com.microblog.models.Notification.NotificationType;
import com.microblog.models.Post;
import com.microblog.models.User;
import com.microblog.repositories.NotificationRepository;

@Component
public class NotificationEventListener {

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private NotificationSseService notificationPusher;

  private void handleEvent(NotificationType type, User actor, User recipient, Post post) {
    if (actor.getId().equals(recipient.getId())) {
      return;
    }
    Notification notification = new Notification(type, recipient, actor, post);
    Notification saved = notificationRepository.save(notification);
    NotificationView view = NotificationView.getNotificationView(saved);
    notificationPusher.sendNotification(recipient.getId(), view);
  }

  @EventListener
  public void handleLikeEvent(LikeEvent event) {
    handleEvent(
        NotificationType.LIKE,
        event.getLike().getUser(),
        event.getLike().getPost().getAuthor(),
        event.getLike().getPost());
  }

  @EventListener
  public void handleUnlikeEvent(UnlikeEvent event) {
    handleEvent(
        NotificationType.UNLIKE,
        event.getLike().getUser(),
        event.getLike().getPost().getAuthor(),
        event.getLike().getPost());
  }

  @EventListener
  public void handleFollowEvent(FollowEvent event) {
    handleEvent(
        NotificationType.FOLLOW,
        event.getFollows().getFollower(),
        event.getFollows().getFollowee(),
        null);
  }

  @EventListener
  public void handleUnfollowEvent(UnfollowEvent event) {
    handleEvent(
        NotificationType.UNFOLLOW,
        event.getFollows().getFollower(),
        event.getFollows().getFollowee(),
        null);
  }

}
