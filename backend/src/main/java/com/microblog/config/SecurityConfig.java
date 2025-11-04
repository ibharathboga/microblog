package com.microblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.microblog.AuthFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

  @Bean
  AuthFilter authFilter() {
    return new AuthFilter();
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, AuthFilter authFilter) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(withDefaults())
        .csrf(csrf -> csrf.disable())
        .formLogin(login -> login.disable())
        .httpBasic(basic -> basic.disable());
    return http.build();
  }
}
