package com.salatin.notification.service;

import com.salatin.notification.model.MessageType;
import com.salatin.notification.model.Notification;
import com.salatin.notification.model.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {
    private final NotificationService notificationService;

    @RabbitListener(queues = "notifications-queue")
    public void receiveNotification(Notification notification) {
        var webSocketMessage = WebSocketMessage.builder()
                .userId(notification.userId())
                .message(notification.message())
                .type(MessageType.NOTIFICATION)
                .build();
        notificationService.sendToUser(webSocketMessage);
    }
}
