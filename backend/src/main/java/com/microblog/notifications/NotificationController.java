package com.microblog.notifications;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.microblog.dto.NotificationView;
import com.microblog.services.CurrentUserService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

  @Autowired
  private NotificationService notificationService;

  @Autowired
  private NotificationSseService notificationSseService;

  @Autowired
  private CurrentUserService currentUser;

  @GetMapping
  public List<NotificationView> getNotifications() {
    return notificationService.getUserNotifications();
  }

  @GetMapping("/unread")
  public List<NotificationView> getUnreadNotifications() {
    return notificationService.getUnreadNotifications();
  }

  @GetMapping("/unread/count")
  public ResponseEntity<Long> getUnreadCount() {
    long count = notificationService.getUnreadCount();
    return ResponseEntity.ok(count);
  }

  @PostMapping("/{id}/read")
  public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
    notificationService.markAsRead(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/read-all")
  public ResponseEntity<Void> markAllAsRead() {
    notificationService.markAllAsRead();
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe() {
    return notificationSseService.subscribe(currentUser.getId());
  }

}
