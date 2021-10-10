package io.codepool.springchallenge.common.pojo.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * The Login Response.
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {

    private String username;
    private String JWTToken;
    private BigDecimal deposit;
}
