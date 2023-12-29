package com.safeway.userservice.repository;

import com.safeway.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findFirstByEmail(String email);

    Optional<User> findTopById(Long id);

    Optional<User> findFirstByMobile(String mobile);

    boolean existsByEmailOrMobile(String email, String mobile);

    @Modifying
    @Transactional
    @Query("update User u set u.password = :password where u.id = :id")
    void updateUserPasswordById(@Param("password") String password, @Param("id") Long id);


//    @Modifying
//    @Transactional
//    @Query("select u from User u set u.password = :password where u.id = :id")
//    void getUserDetails(@Param("password") String password, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update User u set u.username = :password where u.id = :id")
    void updateUserById(@Param("password") String password, @Param("id") Long id);
}
