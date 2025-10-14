package com.aksa.chatapp.repository;

import com.aksa.chatapp.model.Notification;
import com.aksa.chatapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverAndReadStatusFalse(User receiver);
}
