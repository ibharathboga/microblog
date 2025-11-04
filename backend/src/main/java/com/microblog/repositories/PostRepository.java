package com.microblog.repositories;

import com.microblog.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByAuthorUsername(String username);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findByAuthorUsernameIn(List<String> usernames, Pageable pageable);
}
