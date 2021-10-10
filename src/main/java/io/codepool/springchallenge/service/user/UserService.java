package io.codepool.springchallenge.service.user;

import io.codepool.springchallenge.common.pojo.auth.BaseUserAuthDetails;
import io.codepool.springchallenge.common.pojo.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO registerNewUser(BaseUserAuthDetails registrationRequest);

    UserDTO deleteUser(Long userId);

    UserDTO updateUser(Long userId, BaseUserAuthDetails registrationRequest);

    List<UserDTO> getUsers();

}
