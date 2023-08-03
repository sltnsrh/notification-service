package com.salatin.notification.service;

public interface AuthTokenValidationService {

    boolean isValid(String bearerToken);

}
