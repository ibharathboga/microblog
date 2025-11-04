package com.microblog.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.microblog.dto.PostView;
import com.microblog.models.Post;
import com.microblog.repositories.LikeRepository;

@Service
public class PostMapperService {

  @Autowired
  private LikeRepository likeRepository;

  @Autowired
  private CurrentUserService currentUser;

  public PostView toPostView(Post post, boolean isLiked, long likeCount) {
    PostView dto = new PostView();
    dto.setId(post.getId());
    dto.setAuthorUsername(post.getAuthor().getUsername());
    dto.setContent(post.getContent());
    dto.setCreatedAt(post.getCreatedAt());
    dto.setLiked(isLiked);
    dto.setLikedCount(likeCount);
    return dto;
  }

  public List<PostView> toPostViewList(List<Post> posts) {
    Set<UUID> postIds = posts.stream().map(Post::getId).collect(Collectors.toSet());
    Map<UUID, Long> likeCounts = likeRepository.countLikesForPostIds(postIds);
    Set<UUID> currentUserLikedPostIds = likeRepository.findLikedPostIdsByUserId(currentUser.getId(), postIds);

    return posts.stream().map(
        post -> toPostView(
            post,
            currentUserLikedPostIds.contains(post.getId()),
            likeCounts.getOrDefault(post.getId(), 0L)))
        .collect(Collectors.toList());
  }

  public Page<PostView> toPostViewPage(Page<Post> posts) {
    if (posts.isEmpty())
      return Page.empty();

    Set<UUID> postIds = posts.stream().map(Post::getId).collect(Collectors.toSet());
    Map<UUID, Long> likeCounts = likeRepository.countLikesForPostIds(postIds);
    Set<UUID> currentUserLikedPostIds = likeRepository.findLikedPostIdsByUserId(currentUser.getId(), postIds);

    return posts.map(
        post -> toPostView(
            post,
            currentUserLikedPostIds.contains(post.getId()),
            likeCounts.getOrDefault(post.getId(), 0L)));
  }
}
