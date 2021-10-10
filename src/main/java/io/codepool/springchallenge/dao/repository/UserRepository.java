package io.codepool.springchallenge.dao.repository;

import io.codepool.springchallenge.dao.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * The interface User repository.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByIdAndActive(Long id, Boolean active);

    List<UserEntity> findByActive(Boolean active);

    UserEntity findByUsername(String username);
}
