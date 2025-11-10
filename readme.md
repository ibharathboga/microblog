# Microblog

Microblog is a capstone project inspired by core microblogging features similar to platforms like X (twitter). It is built to demonstrate proficiency in Spring Boot backend and React frontend development, with a focus on event-driven design using Server-Sent Events (SSE) for real-time notifications and feed updates. Firebase is integrated solely for authentication.

## Project Overview

Microblog enables users to create/delete posts, like/unlike posts, follow/unfollow other users, and view personalized or public feeds with live updates. The project tackles common challenges such as query efficiency by reducing the N+1 problem through batch queries and implements contextual UI elements like follow/unfollow buttons relative to the current user and showing contextual like status for posts relative to the current user.

## Key Features

- Create and delete posts
- Like and unlike posts with user-relative status
- Follow and unfollow users with contextual button states
- Public and following feeds with live real-time updates using SSE
- Real-time notification system with mark all as read functionality
- Search users and view liked posts
- Backend performance optimized by reducing N+1 query problem using batch queries
- SSE client implemented with fetch-event-source library for fault-tolerant persistent connections

## Technologies Used

- Backend: Spring Boot with REST APIs, PostgreSQL as the database
- Frontend: React with shadcn UI components styled using Tailwind CSS
- Authentication: Firebase Authentication for user login and token verification
- Real-time Updates: Server-Sent Events (SSE) for feed and notification streaming
- Query Optimization: Batch fetching to improve database call efficiency   

## Technical Highlights

- Event-driven architecture demonstrated through SSE implementation connecting backend events with frontend reactive updates.
- Query optimization reducing the N+1 select problem to minimal batch queries, improving performance.
- Contextual reactive UI states (like follow/unfollow) calculated based on the current user’s relationship data dynamically.
- Usage of the fetch-event-source library for SSE.

## Learning Outcomes
This project serves to consolidate knowledge in:

- Spring Boot REST API design with event publishing.
- React patterns integrating with real-time SSE data streams. 
- Effective use of Firebase Authentication integrated in full-stack systems.
- Database performance tuning to avoid common pitfalls like N+1 query issues.
- Implementing core microblogging functionalities inspired by Twitter’s feature set.