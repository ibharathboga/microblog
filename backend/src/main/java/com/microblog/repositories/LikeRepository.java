package com.microblog.repositories;

import com.microblog.models.Like;
import com.microblog.models.LikeId;
import com.microblog.models.Post;
import com.microblog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;
import java.util.stream.Collectors;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

  List<Like> findByUser(User user);

  List<Like> findByPost(Post post);

  Optional<Like> findByUserAndPost(User user, Post post);

  void deleteByUserAndPost(User user, Post post);

  // returns total like count for a single post
  long countByPost(Post post);

  // core query for post mapper service (gets liked counts for multiple posts)
  @Query("""
          select l.post.id, count(l)
          from Like l
          where l.post.id in :postIds
          group by l.post.id
      """)
  List<Object[]> findLikeCountByPostIds(@Param("postIds") Set<UUID> postIds);

  // query postprocessor: converts result list to [post id -> like count] map
  default Map<UUID, Long> countLikesForPostIds(Set<UUID> postIds) {
    return findLikeCountByPostIds(postIds)
        .stream()
        .collect(Collectors.toMap(
            row -> (UUID) row[0],
            row -> (Long) row[1]));
  }

  // core query for post mapper service (gets liked post ids for a user)
  @Query("""
              select l.post.id
              from Like l
              where l.user.id = :userId and l.post.id in :postIds
      """)
  Set<UUID> findLikedPostIdsByUserId(@Param("userId") String userId, @Param("postIds") Set<UUID> postIds);
}
