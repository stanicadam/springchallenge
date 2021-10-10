package io.codepool.springchallenge.common.pojo.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Login request object.
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
}
