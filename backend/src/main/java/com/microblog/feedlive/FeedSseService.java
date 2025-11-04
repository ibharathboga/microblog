package com.microblog.feedlive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.microblog.events.PostAddEvent;
import com.microblog.models.User;
import com.microblog.services.FollowsService;

@Service
public class FeedSseService {

  @Autowired
  private FollowsService followsService;

  private final Map<String, SseEmitter> publicEmitters = new ConcurrentHashMap<>();
  private final Map<String, SseEmitter> followingEmitters = new ConcurrentHashMap<>();

  private void registerEmitterCallbacks(Map<String, SseEmitter> map, String key, SseEmitter emitter) {
    emitter.onCompletion(() -> map.remove(key));
    emitter.onTimeout(() -> map.remove(key));
    emitter.onError(_ -> map.remove(key));
  }

  public SseEmitter subscribePublicFeed(String connectionId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    publicEmitters.put(connectionId, emitter);
    registerEmitterCallbacks(publicEmitters, connectionId, emitter);
    return emitter;
  }

  public SseEmitter subscribeFollowingFeed(String userId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    followingEmitters.put(userId, emitter);
    registerEmitterCallbacks(followingEmitters, userId, emitter);
    return emitter;
  }

  private void sendJsonToPublic(Map<String, Object> payload) {
    var disconnected = new ArrayList<String>();

    for (var entry : publicEmitters.entrySet()) {
      try {
        entry.getValue().send(SseEmitter.event()
            .name("NEW_POST")
            .id(String.valueOf(System.currentTimeMillis()))
            .data(payload));
      } catch (IOException e) {
        disconnected.add(entry.getKey());
      }
    }

    disconnected.forEach(publicEmitters::remove);
  }

  private void sendJsonToFollowers(String userid, Map<String, Object> payload) {
    List<User> followers = followsService.getFollowers(userid);

    if (followers == null || followers.isEmpty()) {
      return;
    }

    for (User follower : followers) {
      SseEmitter emitter = followingEmitters.get(follower.getId());

      if (emitter == null)
        continue;

      try {
        emitter.send(
            SseEmitter.event()
                .name("following-feed")
                .id(String.valueOf(System.currentTimeMillis()))
                .data(payload));
      } catch (IOException e) {
        System.out.println("Error sending to follower " + follower.getId() + ": " + e.getMessage());
        followingEmitters.remove(follower.getId());
      }
    }
  }

  public void pingCheck() {
    sendJsonToPublic(Map.of("status", "ok", "pingCheck", "invoked"));
  }

  public void pushNewPost(PostAddEvent event) {
    var post = event.getPost();

    Map<String, Object> payload = Map.of(
        "type", "NEW_POST",
        "post", Map.of(
            "id", post.getId(),
            "content", post.getContent(),
            "author", Map.of("id", post.getAuthor().getId(), "username", post.getAuthor().getUsername()),
            "timestamp", post.getCreatedAt()));

    System.out.println(payload);
    sendJsonToPublic(payload);
    sendJsonToFollowers(post.getAuthor().getId(), payload);
  }
}
