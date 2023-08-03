package com.salatin.notification.service.impl;

import com.salatin.notification.model.WebSocketMessage;
import com.salatin.notification.service.NotificationStorageService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotificationStorageServiceImpl implements NotificationStorageService {

    @Override
    public void saveToWaitingList(WebSocketMessage socketMessage) {

    }

    @Override
    public List<WebSocketMessage> getWaitingMessages(String userId) {
        return null;
    }
}
