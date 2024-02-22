package com.safeway.userservice.service;

import com.safeway.userservice.dto.UserDetailsDao;
import com.safeway.userservice.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    User saveUser(User user);

    List<UserRole> saveUserRole(List<UserRole> userRoles);

    List<User> getAllUser();

    Page<User> getAllUser(Pageable pageable);

    User getUserById(Long id);

    Set<Long> getAllUserIdByRoleId(Long roleId);

    List<User> getAllUserByRoleId(Long roleId);

    User updateUser(Long id, User user);

    void updateMobileData(Long userId, String regToken, String deviceKey);

    void updatePassword(Long userId, String password);


    void deleteUserRoles(Long userId, Set<Long> roleIds);

    void deleteUser(Long id);

    User findUserByEmail(String email);

    User findUserByMobile(String mobile);

    UserDetailsDao getUserDetailsById(Long id);

    UserDetailsDao getUserDetailsByEmail(String email);

    UserDetailsDao getUserDetailsByMobile(String mobile);

    public List<UserVehicle> saveUserVehicle(List<UserVehicle> userVehicles);

    public void deleteUserVehicles(Long userId, Set<Long> vehicleIds);
}
