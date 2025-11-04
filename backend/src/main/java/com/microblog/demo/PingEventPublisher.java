package com.microblog.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PingEventPublisher {

  @Autowired
  private ApplicationEventPublisher publisher;

  public void publishPing(String message) {
    publisher.publishEvent(new PingEvent(this, message));
  }
}
