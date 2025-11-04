package com.microblog.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

@Configuration
public class FirebaseConfig {

  @Bean
  FirebaseApp firebaseApp() throws IOException {
    if (!FirebaseApp.getApps().isEmpty()) {
      return FirebaseApp.getInstance();
    }

    InputStream serviceAccount = getClass()
        .getClassLoader()
        .getResourceAsStream("firebaseServiceAccountKey.json");

    if (serviceAccount == null) {
      throw new IOException("firebaseServiceAccountKey.json not found in resources.");
    }

    FirebaseOptions options = FirebaseOptions
        .builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build();

    return FirebaseApp.initializeApp(options);
  }

  @Bean
  FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
    return FirebaseAuth.getInstance(firebaseApp);
  }
}
