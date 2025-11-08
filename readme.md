# Microblog

Microblog is a full-stack social networking app inspired by **X (Twitter)**, featuring real-time updates using **Server-Sent Events (SSE)** and authentication via **Firebase**.  
The project demonstrates modern full-stack development practices using **React**, **Spring Boot**, and **PostgreSQL**.

---

## 🧠 Overview

The goal of this project is to build a microblogging application where users can:
- Register and authenticate using Firebase
- Create, delete, like, and follow other users
- Receive real-time notifications and feed updates using SSE
- Search users and explore public or following feeds with pagination

---

## ⚙️ Tech Stack

| Layer | Technology |
|-------|-------------|
| Frontend | React (TypeScript), Tailwind CSS |
| Backend | Spring Boot |
| Database | PostgreSQL |
| Authentication | Firebase Authentication |
| Real-Time Communication | Server-Sent Events (SSE) |

---

## 🚀 Features

- User Registration and Login via Firebase  
- Create and Delete Posts  
- Like / Unlike Functionality  
- Follow / Unfollow Users  
- Search Users and Posts  
- Feed: Public / Following with Pagination  
- Real-time Notification System  
- Real-time Feed Update on New Posts  

---

## 🔄 Real-Time Updates (SSE)

**Server-Sent Events (SSE)** are used for pushing real-time updates from the server to the client over a single HTTP connection.  
SSE is simple to implement and ideal for use cases where only the server sends updates — such as live feeds, notifications, and post updates.

**Why SSE?**
- Lightweight compared to WebSockets  
- Native browser support using `EventSource` API  
- Simple reconnection handling  

Frontend uses the `fetch-event-source` library for stable event streaming.

---

## 🧩 Key Learnings & Challenges

- Modern React practices using Hooks and Context API  
- Database relations and setup using JPA and PostgreSQL  
- Solving **N + 1 problem** and applying batch queries  
- Implementing **composite keys** using `@Embeddable`  
- Structuring code using **Hexagonal Architecture (Ports & Adapters)**  
- Implementing a **Notification System** backed by domain events  
- Using **DTOs and Mapper Services** for clean data transfer  
- Git-based version control and branching workflows  
- Understanding **Pub/Sub pattern** and **intra-application event handling**

---

## 🧠 Spring Boot Highlights

- Clean service and DTO layers with interface-driven design  
- Real-time communication via SSE endpoints  
- Intra-application events using Spring’s `ApplicationEventPublisher`  
- Secure endpoints with Firebase token verification middleware  

---

## 🔮 Future Scope

- Implement batching and scheduling strategies for push updates  
- Introduce message brokers like Kafka or RabbitMQ for high write volume  
- Expand SSE usage for reflecting more events (likes, follows, deletes)  
- Add **Comments** feature  
- Use **Aspect-Oriented Programming (AOP)** for cross-cutting concerns  

---

## 🧑‍💻 Author

**Bharath Chandra**  
Full Stack Developer — React | Spring Boot | PostgreSQL  
Building real-time, event-driven applications and exploring modern backend architectures.

---