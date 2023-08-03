package com.salatin.notification.service;

import com.salatin.notification.model.MessageType;
import com.salatin.notification.model.WebSocketMessage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthTokenUpdateService {
    private final AuthTokenValidationService validationService;
    private final SessionInfoStorageService sessionInfoStorageService;

    public void update(WebSocketSession session, WebSocketMessage message) {
        if (message.type().equals(MessageType.UPDATE)) {
            var userId = message.userId();
            var token = message.jwt();
            var tokenIsValid = validationService.isValid(token);

            if (tokenIsValid) {
                sessionInfoStorageService.updateToken(userId, token);
                log.info("Token successfully updated. User id: " + userId);
            } else {
                log.error("Invalid auth token {}, can't establish connection", token);
                sessionInfoStorageService.remove(userId);
                try {
                    session.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
