package com.safeway.userservice.service;

import com.safeway.userservice.dto.UserDetailsDao;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.entity.UserRoles;
import com.safeway.userservice.entity.admin.Role;
import com.safeway.userservice.repository.UseRolesRepository;
import com.safeway.userservice.repository.UserRepository;
import com.safeway.userservice.repository.admin.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    private RolesRepository rolesRepository;
    private UseRolesRepository useRolesRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UseRolesRepository useRolesRepository) {
        this.userRepository = userRepository;
        this.useRolesRepository = useRolesRepository;
    }

    @Override
    public Optional<UserDetailsDao> getUserDetails(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User user1 = user.get();
            UserDetailsDao userDetailsDao = new UserDetailsDao();
            userDetailsDao.setId(user1.getId());
            userDetailsDao.setUsername(user1.getUsername());
            userDetailsDao.setEmail(user1.getEmail());
            userDetailsDao.setMobile(user1.getMobile());
            userDetailsDao.setPassword(user1.getPassword());
            List<UserRoles> roleList = useRolesRepository.findAllByUserId(user.get().getId());
            userDetailsDao.setRoles(roleList.stream().map(r -> new Role(r.getRoleId())).collect(Collectors.toList()));
            userDetailsDao.setPermissions(List.of());
            return Optional.of(userDetailsDao);

        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {
        return null;
    }

    @Override
    public User saveUser(User user) {
        String password = user.getPassword();
        // user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
