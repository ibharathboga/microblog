package com.microblog.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.microblog.models.User;
import com.microblog.repositories.UserRepository;

@RestController
@RequestMapping("/mocktest")
public class MockUserDemo {

  @Value("${firebase_api_key}")
  private String FIREBASE_API_KEY;

  @Value("${firebase_customid_verify_endpoint}")
  private String FIREBASE_CUSTOMID_VERIFY_ENDPOINT;

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }

  // GET /mocktest/custom-token?id=testUser123
  // returns just the custom token for the UID
  @GetMapping("/custom-token")
  public String generateCustomTokenOnly(@RequestParam("id") String uid) throws FirebaseAuthException {
    return FirebaseAuth.getInstance().createCustomToken(uid);
  }

  // GET /mocktest?id=testUser123 - returns Firebase ID token for given UID
  @GetMapping
  public String getIdTokenForUser(@RequestParam("username") String username) throws Exception {
    User user = userRepository.findByUsername(username).orElseThrow();
    String customToken = FirebaseAuth.getInstance().createCustomToken(user.getId());
    String endpoint = FIREBASE_CUSTOMID_VERIFY_ENDPOINT + FIREBASE_API_KEY;
    String body = String.format("{\"token\":\"%s\",\"returnSecureToken\":true}", customToken);

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpoint))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = mapper.readTree(response.body());
    return jsonNode.get("idToken").asText();
  }
}
