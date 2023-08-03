package com.salatin.notification.model;

import org.springframework.web.socket.WebSocketSession;

public record UserSessionInfo(
        WebSocketSession session,
        String jwt
) {}
