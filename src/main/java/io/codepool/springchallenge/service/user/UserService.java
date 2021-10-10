package io.codepool.springchallenge.service.user;

import io.codepool.springchallenge.common.pojo.auth.BaseUserAuthDetails;
import io.codepool.springchallenge.common.pojo.auth.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(BaseUserAuthDetails registrationRequest);

    UserDTO getById(Long id);

    UserDTO deleteUser(Long userId);

    UserDTO updateUser(Long userId, BaseUserAuthDetails registrationRequest);

    List<UserDTO> getUsers();

}
