package com.microblog.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class AppEventPublisher {
  @Autowired
  private ApplicationEventPublisher publisher;

  public void publish(Object event) {
    publisher.publishEvent(event);
  }
}