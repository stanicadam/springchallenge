package io.codepool.springchallenge.controller;

import io.codepool.springchallenge.common.pojo.auth.BaseUserAuthDetails;
import io.codepool.springchallenge.common.pojo.auth.UserDTO;
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
    public void fakeLogin(@ApiParam("loginRequest") @RequestBody BaseUserAuthDetails loginRequest) {
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
    public ResponseEntity<UserDTO> createUser(@RequestBody BaseUserAuthDetails registrationRequest) {
        return new ResponseEntity<>(
                userService.createUser(registrationRequest),
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
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") Long userId, @RequestBody BaseUserAuthDetails updateRequest) {
        return new ResponseEntity<>(
                userService.updateUser(userId, updateRequest),
                HttpStatus.OK);
    }


    /**
     * Method to delete User
     *
     * @param userId the user id
     * @return void
     */
    @ApiOperation(value = "Delete User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header",
                    required = true)
    })
    @DeleteMapping(value = "/delete/{userId}", produces = "application/json")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(
                userService.deleteUser(userId),
                HttpStatus.OK);
    }

    /**
     * Method to get User by Id
     *
     * @param userId the user id
     * @return UserDTO
     */
    @ApiOperation(value = "Get User By Id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header",
                    required = true)
    })
    @GetMapping(value = "/get/{userId}", produces = "application/json")
    public ResponseEntity<UserDTO> getById(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(
                userService.getById(userId),
                HttpStatus.OK);
    }

    /**
     * Method to get all users
     *
     * @return List of User DTO
     */
    @ApiOperation(value = "Get All Users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header",
                    required = true)
    })
    @PostMapping(value = "/list", produces = "application/json")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(
                userService.getUsers(),
                HttpStatus.OK);
    }

}
