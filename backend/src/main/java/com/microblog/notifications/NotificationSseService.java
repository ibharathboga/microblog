package com.microblog.notifications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microblog.dto.NotificationView;
import com.microblog.events.FollowEvent;
import com.microblog.events.LikeEvent;
import com.microblog.events.UnfollowEvent;
import com.microblog.events.UnlikeEvent;
import com.microblog.models.Follows;

@Service
public class NotificationSseService implements NotificationPusher {
  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

  @Autowired
  private ObjectMapper objectMapper;

  public SseEmitter subscribe(String userId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

    emitters.put(userId, emitter);
    emitter.onCompletion(() -> emitters.remove(userId));
    emitter.onTimeout(() -> emitters.remove(userId));
    emitter.onError((error) -> emitters.remove(userId));

    System.out.println("NotificationSseService: subscribe");
    return emitter;
  }

  private void sendJsonEvent(String targetUserId, Map<String, Object> payload) {
    SseEmitter emitter = emitters.get(targetUserId);
    if (emitter == null)
      return;

    try {
      emitter.send(SseEmitter.event()
          .id(String.valueOf(System.currentTimeMillis()))
          .name("notification")
          .data(payload));
    } catch (IOException e) {
      System.out.println("[NotificationSseService] sendJsonEvent: error caught");
      System.out.println(e.getMessage());
      emitters.remove(targetUserId);
    }
  }

  public void sendNotification(String userId, NotificationView notification) {
    SseEmitter emitter = emitters.get(userId);
    if (emitter == null || emitters.isEmpty()) {
      System.out.println("No active emitters for user: " + userId);
      return;
    }

    System.out.println("Sending notification to user " + userId + ": " + notification.getType());

    try {
      String jsonData = objectMapper.writeValueAsString(notification);
      emitter.send(
          SseEmitter.event()
              .name("notification")
              .data(jsonData));
    } catch (IOException e) {
      System.out.println("Error sending notification to user " + userId + ": " + e.getMessage());
      emitters.remove(userId);
    }
  }

  public void pingCheck() {
    var keysToRemove = new ArrayList<String>();
    for (var entry : emitters.entrySet()) {
      Map<String, String> payload = Map.of(entry.getKey(), "helloooooo");
      try {
        entry.getValue().send(SseEmitter.event()
            .name("response")
            .data(payload));
      } catch (IOException e) {
        System.out.println("Error sending SSE event: " + e.getMessage());
        keysToRemove.add(entry.getKey());
      }
    }
    keysToRemove.forEach(emitters::remove);
  }

  @Override
  public void pushLikeNotification(LikeEvent event) {
    var like = event.getLike();
    String targetUserId = like.getPost().getAuthor().getId();

    Map<String, Object> payload = Map.of(
        "type", "LIKE",
        "actor", Map.of(
            "id", like.getUser().getId(),
            "username", like.getUser().getUsername()),
        "post", Map.of("id", like.getPost().getId()),
        "timestamp", System.currentTimeMillis());

    sendJsonEvent(targetUserId, payload);
  }

  @Override
  public void pushUnlikeNotification(UnlikeEvent event) {
    var like = event.getLike();
    String targetUserId = like.getPost().getAuthor().getId();

    Map<String, Object> payload = Map.of(
        "type", "UNLIKE",
        "actor", Map.of(
            "id", like.getUser().getId(),
            "username", like.getUser().getUsername()),
        "post", Map.of("id", like.getPost().getId()),
        "timestamp", System.currentTimeMillis());

    sendJsonEvent(targetUserId, payload);
  }

  @Override
  public void pushFollowNotification(FollowEvent followEvent) {
    Follows follow = followEvent.getFollows();
    String targetUserId = follow.getFollowee().getId();

    Map<String, Object> payload = Map.of(
        "type", "FOLLOW",
        "actor", Map.of(
            "id", follow.getFollower().getId(),
            "username", follow.getFollower().getUsername()),
        "timestamp", System.currentTimeMillis());

    sendJsonEvent(targetUserId, payload);
  }

  @Override
  public void pushUnfollowNotification(UnfollowEvent unfollowEvent) {
    Follows follow = unfollowEvent.getFollows();
    String targetUserId = follow.getFollowee().getId();

    Map<String, Object> payload = Map.of(
        "type", "UNFOLLOW",
        "actor", Map.of(
            "id", follow.getFollower().getId(),
            "username", follow.getFollower().getUsername()),
        "timestamp", System.currentTimeMillis());

    sendJsonEvent(targetUserId, payload);
  }
}
