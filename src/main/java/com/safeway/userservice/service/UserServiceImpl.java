package com.safeway.userservice.service;

import com.safeway.userservice.dto.UserDetailsDao;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.entity.admin.Role;
import com.safeway.userservice.repository.UserRepository;
import com.safeway.userservice.repository.admin.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository ) {
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
            User user1 = user.get();
            UserDetailsDao userDetailsDao = new UserDetailsDao();
            userDetailsDao.setId(user1.getId());
            userDetailsDao.setUsername(user1.getUsername());
            userDetailsDao.setEmail(user1.getEmail());
            userDetailsDao.setMobile(user1.getMobile());
            userDetailsDao.setPassword(user1.getPassword());
            userDetailsDao.setRoles(user1.getRoles());
            userDetailsDao.setPermissions(Set.of());
            return Optional.of(userDetailsDao);
        }
        return Optional.empty();
    }


    @Override
    public Optional<UserDetailsDao> getUserDetailsById(Long id) {
        Optional<User> user = getUserById(id);
        if (user.isPresent()) {
            User user1 = user.get();
            UserDetailsDao userDetailsDao = new UserDetailsDao();
            userDetailsDao.setId(user1.getId());
            userDetailsDao.setUsername(user1.getUsername());
            userDetailsDao.setEmail(user1.getEmail());
            userDetailsDao.setMobile(user1.getMobile());
            userDetailsDao.setPassword(user1.getPassword());
//            List<UserRoles> roleList = useRolesRepository.findAllByUserId(user.get().getId());
//            userDetailsDao.setRoles(roleList.stream().map(r -> new Role(r.getRoleId().getId())).collect(Collectors.toList()));
            userDetailsDao.setPermissions(Set.of());
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
        userRepository.updateUserById(user.getMobile(), id);
        return null;
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
