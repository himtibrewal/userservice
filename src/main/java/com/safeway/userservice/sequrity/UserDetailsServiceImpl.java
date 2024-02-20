package com.safeway.userservice.sequrity;

import com.safeway.userservice.dto.UserDetailsDao;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserDetailsImpl.build(userService.getUserDetails(username));
    }

    @Transactional
    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
            return UserDetailsImpl.build(userService.getUserDetailsById(Long.parseLong(userId)));
    }

}