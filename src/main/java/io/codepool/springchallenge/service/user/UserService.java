package io.codepool.springchallenge.service.user;

import io.codepool.springchallenge.common.pojo.RegistrationRequest;
import io.codepool.springchallenge.common.pojo.UserDTO;

public interface UserService {

    UserDTO registerNewUser(RegistrationRequest registrationRequest);
}
