package com.salatin.notification.model;

import lombok.Builder;

@Builder
public record WebSocketMessage(
        String userId,
        String jwt,
        String message,
        MessageType type
) {}
