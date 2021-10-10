package io.codepool.springchallenge.service.user;

import io.codepool.springchallenge.common.pojo.auth.BaseUserAuthDetails;
import io.codepool.springchallenge.common.pojo.auth.CreateUpdateUserRequest;
import io.codepool.springchallenge.common.pojo.auth.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(CreateUpdateUserRequest registrationRequest);

    UserDTO getById(Long id);

    UserDTO deleteUser(Long userId);

    UserDTO updateUser(Long userId, CreateUpdateUserRequest registrationRequest);

    List<UserDTO> getUsers();

}
