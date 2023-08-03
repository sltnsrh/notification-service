package com.salatin.notification.service;

import com.salatin.notification.model.WebSocketMessage;

public interface NotificationService {

    void sendToUser(WebSocketMessage webSocketMessage);
}
