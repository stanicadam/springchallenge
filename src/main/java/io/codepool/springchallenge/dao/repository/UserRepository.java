package io.codepool.springchallenge.dao.repository;

import io.codepool.springchallenge.dao.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface User repository.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}
