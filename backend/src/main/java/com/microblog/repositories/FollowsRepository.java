package com.microblog.repositories;

import com.microblog.models.Follows;
import com.microblog.models.FollowsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface FollowsRepository extends JpaRepository<Follows, FollowsId> {
  List<Follows> findByFollowee_Id(String followeeId);

  List<Follows> findByFollower_Id(String followerId);

  @Query("""
      SELECT f.followee.id
      FROM Follows f
      WHERE f.follower.id = :currentUserId
      AND f.followee.id IN :targetUserIds
      """)
  Set<String> findFollowedUserIds(
      @Param("currentUserId") String currentUserId,
      @Param("targetUserIds") Collection<String> targetUserIds);

  boolean existsByFollower_IdAndFollowee_Id(String followerId, String followeeId);
}
