package com.microblog.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.microblog.events.LikeEvent;
import com.microblog.events.UnlikeEvent;
import com.microblog.models.Follows;

@Component
public class PingEventSubscriber {

  @Autowired
  private SseService sseService;

  @EventListener
  public void onPingEvent(PingEvent event) {
    sseService.sendPing(event.getMessage());
    System.out.println("onPingEvent: invoked");
  }

  @EventListener
  public void onLikeEvent(LikeEvent event) {
    System.out.println("onLikeEvent: invoked");
    sseService.sendPing("onLikeEvent: invoked");
  }

  @EventListener
  public void onUnlikeEvent(UnlikeEvent event) {
    System.out.println("onUnlikeEvent: invoked");
    sseService.sendPing("onUnlikeEvent: invoked");
  }

  @EventListener
  public void onFollowEvent(Follows event) {
    System.out.println("onFollowEvent: invoked");
    sseService.sendPing("onFollowEvent: invoked");
  }

  @EventListener
  public void onUnfollowEvent(Follows event) {
    System.out.println("onUnfollowEvent: invoked");
    sseService.sendPing("onUnfollowEvent: invoked");
  }

}
