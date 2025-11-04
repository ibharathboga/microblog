package com.microblog.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microblog.models.Notification;
import com.microblog.models.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
  List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);

  List<Notification> findByRecipientAndIsReadFalseOrderByCreatedAtDesc(User recipient);

  long countByRecipientAndIsReadFalse(User recipient);
}
