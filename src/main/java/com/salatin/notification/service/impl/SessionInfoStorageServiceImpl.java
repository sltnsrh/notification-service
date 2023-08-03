package com.salatin.notification.service.impl;

import com.salatin.notification.model.UserSessionInfo;
import com.salatin.notification.service.SessionInfoStorageService;
import org.springframework.stereotype.Service;

@Service
public class SessionInfoStorageServiceImpl implements SessionInfoStorageService {

    @Override
    public UserSessionInfo getByUser(String userId) {
        return null;
    }

    @Override
    public void add(String userId, UserSessionInfo sessionInfo) {

    }

    @Override
    public void remove(String userId) {

    }

    @Override
    public void updateToken(String userId, String newJwt) {

    }
}
