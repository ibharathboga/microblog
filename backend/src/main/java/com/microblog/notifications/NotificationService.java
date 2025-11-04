package com.microblog.notifications;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microblog.dto.NotificationView;
import com.microblog.models.Notification;
import com.microblog.models.User;
import com.microblog.repositories.NotificationRepository;
import com.microblog.repositories.UserRepository;
import com.microblog.services.CurrentUserService;

@Service
public class NotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CurrentUserService currentUserService;

  public List<NotificationView> getUserNotifications() {
    User user = userRepository.findById(currentUserService.getId()).orElseThrow();
    List<Notification> notifications = notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
    return notifications.stream()
        .map(NotificationView::getNotificationView)
        .collect(Collectors.toList());
  }

  public List<NotificationView> getUnreadNotifications() {
    User user = userRepository.findById(currentUserService.getId()).orElseThrow();
    List<Notification> notifications = notificationRepository.findByRecipientAndIsReadFalseOrderByCreatedAtDesc(user);
    return notifications.stream()
        .map(NotificationView::getNotificationView)
        .collect(Collectors.toList());
  }

  public long getUnreadCount() {
    User user = userRepository.findById(currentUserService.getId()).orElseThrow();
    return notificationRepository.countByRecipientAndIsReadFalse(user);
  }

  @Transactional
  public void markAsRead(UUID notificationId) {
    Notification notification = notificationRepository.findById(notificationId).orElseThrow();

    if (!notification.getRecipient().getId().equals(currentUserService.getId())) {
      throw new RuntimeException("Unauthorized access to notification");
    }

    notification.setRead(true);
    notificationRepository.save(notification);
  }

  @Transactional
  public void markAllAsRead() {
    User user = userRepository.findById(currentUserService.getId()).orElseThrow();
    List<Notification> unreadNotifications = notificationRepository
        .findByRecipientAndIsReadFalseOrderByCreatedAtDesc(user);

    unreadNotifications.forEach(n -> n.setRead(true));
    notificationRepository.saveAll(unreadNotifications);
  }

}
