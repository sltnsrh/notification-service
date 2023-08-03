package com.salatin.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salatin.notification.model.MessageType;
import com.salatin.notification.model.WebSocketMessage;
import com.salatin.notification.service.AuthTokenValidationService;
import com.salatin.notification.service.NotificationService;
import com.salatin.notification.service.NotificationStorageService;
import com.salatin.notification.service.SessionInfoStorageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationServiceImpl implements NotificationService {
    private final SessionInfoStorageService sessionInfoStorageService;
    private final NotificationStorageService notificationStorageService;
    private final AuthTokenValidationService validationService;
    private final ObjectMapper objectMapper;

    @Override
    public void sendToUser(WebSocketMessage webSocketMessage) {
        var userId = webSocketMessage.userId();
        var sessionInfo = sessionInfoStorageService.getByUser(userId);
        var session = sessionInfo.session();
        var token = sessionInfo.jwt();
        var isTokenValid = validationService.isValid(token);
        var message = webSocketMessage.message();

        if (session != null && session.isOpen()) {
            if (isTokenValid) {
                try {
                    var webSocketMessageJson = objectMapper.writeValueAsString(webSocketMessage);
                    session.sendMessage(new TextMessage(webSocketMessageJson));
                    return;
                } catch (IOException e) {
                    log.error("Can't send message {} though websocket to user {} ", message, userId , e);
                    notificationStorageService.saveToWaitingList(webSocketMessage);
                }
            } else {
                log.warn("Can't send message to user {} through websocket, token is invalid: {}", userId, token);
                sendUpdateTokenMessageAndCloseSession(userId, session);
                sessionInfoStorageService.remove(userId);
            }
        } else {
            if (session != null) sessionInfoStorageService.remove(userId);
            log.warn("Session is closed. Can't send message {} though websocket to user {}", message, userId);
        }
        notificationStorageService.saveToWaitingList(webSocketMessage);
    }

    private void sendUpdateTokenMessageAndCloseSession(String userId, WebSocketSession session) {
        var updateMessage = WebSocketMessage.builder()
                .userId(userId)
                .message("Invalid token")
                .type(MessageType.UPDATE)
                .build();
        try {
            var updateMessageJson = objectMapper.writeValueAsString(updateMessage);
            session.sendMessage(new TextMessage(updateMessageJson));
            session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
