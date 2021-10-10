package io.codepool.springchallenge.common.services;

import io.codepool.springchallenge.dao.model.UserEntity;
import io.codepool.springchallenge.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * This service allows us to fetch the current logged in user,
 * this functionality will be used accross the system so it's best to externalize it
 * into its own class.
 */
@Component
public class ContextHolderService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }
}
