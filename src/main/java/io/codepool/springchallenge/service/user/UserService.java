package io.codepool.springchallenge.service.user;

import io.codepool.springchallenge.common.pojo.auth.UserCreateUpdateRequest;
import io.codepool.springchallenge.common.pojo.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO registerNewUser(UserCreateUpdateRequest registrationRequest);

    UserDTO updateUser(Long userId, UserCreateUpdateRequest registrationRequest);

    List<UserDTO> getUsers();

}
