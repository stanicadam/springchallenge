package io.codepool.springchallenge.dao.repository;

import io.codepool.springchallenge.dao.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The interface User repository.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Find user by email or username .
     *
     * @param searchCriteria the search criteria
     * @return the user entity
     */
    @Query(value = "SELECT * FROM USER u WHERE u.USERNAME = ?1 OR u.EMAIL = ?1", nativeQuery = true)
    UserEntity findUserByEmailOrUsername(String searchCriteria);


    /**
     * Find duplicate user details.
     * Used for checking if there are any existing users with unique identifiers.
     *
     * @param identifierOne the username/email identifier
     * @param identifierTwo the username/email identifier
     * @return the list
     */
    @Query(value = "SELECT * FROM USER u WHERE u.USERNAME in (:identifierOne, :identifierTwo)\n" +
            "OR u.EMAIL in (:identifierOne, :identifierTwo)", nativeQuery = true)
    List<UserEntity> findDuplicateUserDetails(@Param("identifierOne") String identifierOne,
                                              @Param("identifierTwo") String identifierTwo);

}
