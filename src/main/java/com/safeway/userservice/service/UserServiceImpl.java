package com.safeway.userservice.service;

import com.safeway.userservice.dto.UserDetailsDao;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.entity.admin.Permission;
import com.safeway.userservice.entity.admin.Role;
import com.safeway.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean existsByEmailOrMobile(String email, String mobile) {
        return userRepository.existsByEmailOrMobile(email, mobile);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }


    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    @Override
    public Optional<User> findUserByMobile(String mobile) {
        return userRepository.findFirstByMobile(mobile);
    }


    public void updateUserPasswordById(String password, Long id) {
        userRepository.updateUserPasswordById(password, id);
    }

    // Get By Email
    @Override
    public Optional<UserDetailsDao> getUserDetails(String username) {
        Optional<User> user = findUserByEmail(username);
        if (user.isPresent()) {
            return Optional.of(computeUserDetails(user.get()));
        }
        return Optional.empty();
    }


    @Override
    public Optional<UserDetailsDao> getUserDetailsById(Long id) {
        Optional<User> user = getUserById(id);
        if (user.isPresent()) {
            return Optional.of(computeUserDetails(user.get()));
        }
        return Optional.empty();
    }

    private UserDetailsDao computeUserDetails(User user) {
        Set<String> permissionSet = user.getRoles().stream()
                .map(role -> role.getPermissions().stream()
                        .map(Permission::getPermissionCode).collect(Collectors.toSet()))
                .reduce(new HashSet<>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                });

        Set<String> rolesSet = user.getRoles().stream().map(Role::getRoleCode).collect(Collectors.toSet());


        UserDetailsDao userDetailsDao = new UserDetailsDao();
        userDetailsDao.setId(user.getId());
        userDetailsDao.setUsername(user.getUsername());
        userDetailsDao.setEmail(user.getEmail());
        userDetailsDao.setMobile(user.getMobile());
        userDetailsDao.setPassword(user.getPassword());
        userDetailsDao.setRoles(rolesSet);
        userDetailsDao.setPermissions(permissionSet);
        return userDetailsDao;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findTopById(id);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {
        userRepository.updateUserById(user.getMobile(), id);
        return null;
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
