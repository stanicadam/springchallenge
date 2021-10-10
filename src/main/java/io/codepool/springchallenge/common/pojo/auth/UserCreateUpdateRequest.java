package io.codepool.springchallenge.common.pojo.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Registration request object.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserCreateUpdateRequest {

    private String username;
    private String password;
}
