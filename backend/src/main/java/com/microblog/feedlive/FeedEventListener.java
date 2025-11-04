package com.microblog.feedlive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.microblog.events.PostAddEvent;

@Component
public class FeedEventListener {
  @Autowired
  private FeedSseService feedSseService;

  @EventListener
  public void onPostAddEvent(PostAddEvent postAddEvent) {
    feedSseService.pushNewPost(postAddEvent);
  }

}
