package com.salatin.notification.controller;

import com.salatin.notification.model.WebSocketMessage;
import com.salatin.notification.service.AuthTokenUpdateService;
import com.salatin.notification.service.SessionInfoStorageService;
import com.salatin.notification.service.SessionService;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.WebSocketSession;

@Controller
@RequiredArgsConstructor
@Log4j2
public class WebSocketController {
    private final SessionInfoStorageService sessionInfoStorageService;
    private final AuthTokenUpdateService authTokenUpdateService;
    private final SessionService sessionService;

    @OnOpen
    public void onOpen(WebSocketSession session,
                       @RequestParam("userId") String userId,
                       @RequestParam("token") String token) {
        sessionService.setNewSession(session, userId, token);
    }

    @OnMessage
    public void onMessage(WebSocketSession session, @Payload WebSocketMessage message) {
        authTokenUpdateService.update(session, message);
    }

    @OnClose
    public void onClose(WebSocketSession session, @RequestParam("userId") String userId) {
        sessionInfoStorageService.remove(userId);
        log.info("User {} closed the session {}", userId, session.getId());
    }
}
