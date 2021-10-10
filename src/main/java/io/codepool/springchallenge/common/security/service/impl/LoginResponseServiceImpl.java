package io.codepool.springchallenge.common.security.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codepool.springchallenge.common.pojo.auth.LoginResponse;
import io.codepool.springchallenge.common.security.service.LoginResponseService;
import io.codepool.springchallenge.dao.model.UserEntity;
import io.codepool.springchallenge.dao.repository.UserRepository;
import org.springframework.stereotype.Service;


/**
 * The implementation of the Login response service interface.
 * Upon successful log in, we will return the User contract here.
 * The user contract could then house the JWT token, the username, and any other
 * information that is necessary for the mobile app/front end requests terminating here.
 */
@Service
public class LoginResponseServiceImpl implements LoginResponseService {

    private final UserRepository userRepository;

    public LoginResponseServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String generateLoginResponse(String JWT, String username) throws JsonProcessingException {
        UserEntity user = userRepository.findByUsername(username);

        LoginResponse userResponse = new LoginResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setDeposit(user.getDeposit());
        userResponse.setJWTToken(JWT);
        userResponse.setRole(user.getRole());
        return new ObjectMapper().writeValueAsString(userResponse);
    }
}
