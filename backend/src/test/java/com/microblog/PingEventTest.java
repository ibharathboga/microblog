package com.microblog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.microblog.demo.PingEventPublisher;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PingEventTest extends BaseSecurityTest {

  @Autowired
  private PingEventPublisher pingEventPublihser;

  @Test
  void triggerPingEvent() {
    pingEventPublihser.publishPing("this is ping event from ping event test");
  }

}
