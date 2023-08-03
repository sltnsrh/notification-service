package com.salatin.notification.service;

import com.salatin.notification.model.UserSessionInfo;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
@Log4j2
public class SessionService {
    private final AuthTokenValidationService validationService;
    private final SessionInfoStorageService sessionInfoStorageService;
    private final NotificationStorageService notificationStorageService;
    private final NotificationService notificationService;

    public void setNewSession(WebSocketSession session,
                              String userId,
                              String token) {
        var tokenIsValid = validationService.isValid(token);

        if (tokenIsValid) {
            var userSessionInfo = new UserSessionInfo(session, token);

            sessionInfoStorageService.add(userId, userSessionInfo);
            sendWaitingMessagesIfExist(userId);
        } else {
            log.error("Invalid auth token {}, can't establish connection", token);
            try {
                session.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendWaitingMessagesIfExist(String userId) {
        var waitingMessages = notificationStorageService.getWaitingMessages(userId);
        if (!waitingMessages.isEmpty()) {
            waitingMessages.forEach(notificationService::sendToUser);
        }
    }
}
