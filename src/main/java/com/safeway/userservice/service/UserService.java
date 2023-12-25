package com.safeway.userservice.service;

import com.safeway.userservice.dto.UserDetailsDao;
import com.safeway.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);

    boolean existsByEmailOrMobile(String email, String mobile);

    User saveUser(User user);

    void updateUserPasswordById(String password, Long id);

    //UserRoles saveUserRoles(UserRoles userRoles);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByMobile(String mobile);

    List<User> getAllUser();

    User updateUser(Long id, User user);



    void deleteUser(Long id);

    Optional<UserDetailsDao> getUserDetails(String username);

    Optional<UserDetailsDao> getUserDetailsById(Long id);
}
