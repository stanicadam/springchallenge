package io.codepool.springchallenge.controller;

import io.codepool.springchallenge.common.pojo.RegistrationRequest;
import io.codepool.springchallenge.common.pojo.UserDTO;
import io.codepool.springchallenge.service.user.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * Method to register new user
     *
     * @param registrationRequest the registration request object
     * @return User DTO
     */
    @ApiOperation(value = "Register new User")
    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> saveUser(@RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(
                userService.registerNewUser(registrationRequest),
                HttpStatus.CREATED);
    }
}
