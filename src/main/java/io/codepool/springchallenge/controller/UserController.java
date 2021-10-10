package io.codepool.springchallenge.controller;

import io.codepool.springchallenge.common.pojo.auth.LoginRequest;
import io.codepool.springchallenge.common.pojo.auth.UserCreateUpdateRequest;
import io.codepool.springchallenge.common.pojo.UserDTO;
import io.codepool.springchallenge.service.user.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Fake login. Spring security will take over the logging in.
     *
     * @param loginRequest the login request
     */
    @ApiOperation(value = "Login via Username or Email", consumes = "application/json", produces = "application/json;charset=UTF-8")
    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json;charset=UTF-8")
    public void fakeLogin(@ApiParam("loginRequest") @RequestBody LoginRequest loginRequest) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    /**
     * Method to register new user
     *
     * @param registrationRequest the registration request object
     * @return User DTO
     */
    @ApiOperation(value = "Register new User")
    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserCreateUpdateRequest registrationRequest) {
        return new ResponseEntity<>(
                userService.registerNewUser(registrationRequest),
                HttpStatus.CREATED);
    }

    /**
     * Method to update User
     *
     * @param updateRequest
     * @return User DTO
     */
    @ApiOperation(value = "Update existing User username and pass")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header",
                    required = true)
    })
    @PutMapping(value = "/update/{userId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> saveUser(@PathVariable("userId") Long userId, @RequestBody UserCreateUpdateRequest updateRequest) {
        return new ResponseEntity<>(
                userService.updateUser(userId, updateRequest),
                HttpStatus.OK);
    }

    /**
     * Method to get all users
     *
     * @return List of User DTO
     */
    @ApiOperation(value = "Get Users")
    @PostMapping(value = "/list", produces = "application/json")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(
                userService.getUsers(),
                HttpStatus.OK);
    }

}
