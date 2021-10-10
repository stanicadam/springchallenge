package io.codepool.springchallenge.common.pojo.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Login Response.
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {

    private String email;
    private String username;
    private String JWTToken;
}
