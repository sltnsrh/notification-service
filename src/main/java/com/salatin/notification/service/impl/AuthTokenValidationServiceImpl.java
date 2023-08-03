package com.salatin.notification.service.impl;

import com.salatin.notification.service.AuthTokenValidationService;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenValidationServiceImpl implements AuthTokenValidationService {
    @Override
    public boolean isValid(String bearerToken) {
        return false;
    }
}
