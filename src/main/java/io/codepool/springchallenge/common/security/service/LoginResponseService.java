package io.codepool.springchallenge.common.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * The interface Login response service.
 * Define all functions needed to happen on log in here.
 */
public interface LoginResponseService {

    /**
     * Generate login response string.
     * Result is the stringification of the JSON user contract
     *
     * @param JWT    the jwt
     * @param username the user username
     * @return the string
     * @throws JsonProcessingException the json processing exception
     */
    String generateLoginResponse(String JWT, String username) throws JsonProcessingException;
}
