package com.safeway.userservice.service;

import com.safeway.userservice.dto.UserDetailsDao;
import com.safeway.userservice.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);

    List<User> getAllUser();

    User updateUser(Long id, User user);

    User saveUser(User user);

    void deleteUser(Long id);

    Optional<UserDetailsDao> getUserDetails(String username);
}
