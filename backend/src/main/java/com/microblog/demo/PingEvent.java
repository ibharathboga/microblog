package com.microblog.demo;

import org.springframework.context.ApplicationEvent;

public class PingEvent extends ApplicationEvent {
  private final String message;

  public PingEvent(Object source, String message) {
    super(source);
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
