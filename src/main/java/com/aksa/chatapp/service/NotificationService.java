package com.aksa.chatapp.service;

import com.aksa.chatapp.model.Notification;
import com.aksa.chatapp.model.User;
import com.aksa.chatapp.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(User receiver, String messageText) {
        Notification notification = new Notification();
        notification.setReceiver(receiver);
        notification.setMessage(messageText);
        notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications(User receiver) {
        return notificationRepository.findByReceiverAndReadStatusFalse(receiver);
    }

    public void markAllAsRead(User receiver) {
        List<Notification> notifications = notificationRepository.findByReceiverAndReadStatusFalse(receiver);
        notifications.forEach(n -> n.setReadStatus(true));
        notificationRepository.saveAll(notifications);
    }

    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setReadStatus(true);
            notificationRepository.save(n);
        });
    }
}
