package com.salatin.notification.service;

import com.salatin.notification.model.UserSessionInfo;

/**
 * Allows to add, get and remove websocket session info from Redis storage
 */
public interface SessionInfoStorageService {

    UserSessionInfo getByUser(String userId);

    void add(String userId, UserSessionInfo sessionInfo);

    void remove(String userId);

    void updateToken(String userId, String newJwt);
}
