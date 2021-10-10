package io.codepool.springchallenge.common.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Registration request object.
 */
@Getter
@Setter
@NoArgsConstructor
public class RegistrationRequest {

    private String email;
    private String username;
    private String password;
}