package com.microblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microblog.demo.PingEventPublisher;

@RestController
@RequestMapping("/ping")
public class PingController {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private PingEventPublisher pingEventPublisher;

  @GetMapping
  public String sayPong() {
    return "pong";
  }

  @GetMapping(value = "/db")
  public String pingDb() {
    try {
      jdbcTemplate.execute("SELECT 1");
      return "pong (DB OK)";
    } catch (Exception e) {
      return "pong (DB FAIL): " + e.getMessage();
    }
  }

  @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> sendPing(@RequestBody String message) {
    pingEventPublisher.publishPing("event: " + message);
    return ResponseEntity.ok("response: " + message);
  }

  // @GetMapping(value = "/ping-notifications")
  // public Map<String, String> ping() {
  // Map<String, String> response = new HashMap<>();
  // response.put("ping", "pong");
  // response.put("server", "spring");
  // notificationSseService.pingCheck();
  // return response;
  // }
}
