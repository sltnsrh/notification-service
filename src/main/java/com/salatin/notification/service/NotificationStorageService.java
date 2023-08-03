package com.salatin.notification.service;

import com.salatin.notification.model.WebSocketMessage;
import java.util.List;

/**
 * Allows to add to Redis storage waiting messages for user while he is offline,
 * and to get list of messages to sent to user when he is already online.
 */
public interface NotificationStorageService {

    void saveToWaitingList(WebSocketMessage socketMessage);

    List<WebSocketMessage> getWaitingMessages(String userId);
}
