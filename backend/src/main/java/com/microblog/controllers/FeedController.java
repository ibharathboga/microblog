package com.microblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.microblog.dto.PostView;
import com.microblog.feedlive.FeedSseService;
import com.microblog.services.CurrentUserService;
import com.microblog.services.FeedService;

@RestController
@RequestMapping("/feed")
public class FeedController {

  @Autowired
  private FeedService feedService;

  @Autowired
  private FeedSseService feedSseService;

  @Autowired
  private CurrentUserService currentUser;

  @GetMapping("/public")
  public Page<PostView> getPublicFeed(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return feedService.getPublicFeed(page, size);
  }

  @GetMapping("/following")
  public Page<PostView> getFollowingFeed(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return feedService.getFollowingFeed(page, size);
  }

  @GetMapping(value = "/subscribe/public", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribePublic() {
    return feedSseService.subscribePublicFeed(currentUser.getId());
  }

  @GetMapping(value = "/subscribe/following", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribeFollowing() {
    return feedSseService.subscribeFollowingFeed(currentUser.getId());
  }
}
