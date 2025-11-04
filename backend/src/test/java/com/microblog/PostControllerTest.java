package com.microblog;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.firebase.auth.FirebaseAuthException;
import com.microblog.controllers.PostsController;
import com.microblog.dto.PostView;
import com.microblog.models.Post;
import com.microblog.repositories.PostRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest extends BaseSecurityTest {

  @Autowired
  private PostsController postController;

  @Autowired
  private PostRepository postRepository;

  @Test
  void addPostTest() throws FirebaseAuthException {
    Post post = new Post();
    post.setContent("this post content is being added at 731pm 15oct");
    PostView saved = postController.addPost(post);

    System.out.println("========addPostTest=========");
    System.out.println("saved post content: " + saved.getContent());
  }

  @Test
  void addRandomPostsTest() throws FirebaseAuthException {
    for (int i = 1; i <= 3; i++) {
      Post post = new Post();
      post.setContent("random post #" + i + " content @ " + System.currentTimeMillis());
      PostView saved = postController.addPost(post);
      System.out.println("========addPostTest " + i + "=========");
      System.out.println("saved post content: " + saved.getContent());
    }
  }

  @Test
  void deletePostTest() throws FirebaseAuthException {
    String id = "9c0a3e53-ab28-4e54-9a2d-c823d3f242aa";
    UUID uuid = UUID.fromString(id);
    postController.deletePost(uuid);
  }

  @Test
  void deleteAllPostsTest() {
    postRepository.deleteAll();
  }

}
