package com.microblog.demo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseService {
  private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  public SseEmitter createEmitter() {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    emitters.add(emitter);

    emitter.onCompletion(() -> emitters.remove(emitter));
    emitter.onTimeout(() -> emitters.remove(emitter));
    emitter.onError((e) -> {
      System.out.println("[SseService]:onError " + e.getMessage());
      emitters.remove(emitter);
    });

    return emitter;
  }

  public void sendPing(String message) {
    System.out.println("SSE SERVICE: sendPing: invoked");
    for (SseEmitter emitter : emitters) {
      try {
        emitter.send(
            SseEmitter.event()
                .name("ping")
                .data(message));
      } catch (IOException e) {
        System.out.println("Error sending SSE event: " + e.getMessage());
        emitters.remove(emitter);
      }
    }
  }
}
