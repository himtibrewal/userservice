package com.safeway.userservice.service;

import com.safeway.userservice.dto.UserDetailsDao;
import com.safeway.userservice.entity.*;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.UserRoleRepository;
import com.safeway.userservice.repository.UserRepository;
import com.safeway.userservice.repository.UserVehicleRepository;
import com.safeway.userservice.service.admin.PermissionService;
import com.safeway.userservice.service.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final UserVehicleRepository userVehicleRepository;

    private final RoleService roleService;

    private final PermissionService permissionService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, UserVehicleRepository userVehicleRepository, RoleService roleService, PermissionService permissionService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userVehicleRepository = userVehicleRepository;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserRole> saveUserRole(List<UserRole> userRoles) {
        return userRoleRepository.saveAll(userRoles);
    }

    public List<UserVehicle> saveUserVehicle (List<UserVehicle> userVehicles) {
        return userVehicleRepository.saveAll(userVehicles);
    }

    @Override
    public void deleteUserVehicles(Long userId, Set<Long> vehicleIds) {
        userVehicleRepository.deleteByUserIdAndVehicleIds(userId, vehicleIds);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "user");
        }
        return user.get();
    }

    @Override
    public Set<Long> getAllUserIdByRoleId(Long roleId) {
        return userRoleRepository.findAllUserIdByRoleId(roleId);
    }

    @Override
    public List<User> getAllUserByRoleId(Long roleId) {
        return userRoleRepository.findAllUserByRoleId(roleId);
    }

    @Override
    public void deleteUserRoles(Long userId, Set<Long> roleIds) {
        userRoleRepository.deleteByUserIdAndRoleIds(userId, roleIds);
    }


    @Override
    public User updateUser(Long id, User user) {
        User updateUser = getUserById(id);
        updateUser.setUsername(user.getUsername());
        updateUser.setEmail(user.getEmail());
        updateUser.setMobile(user.getMobile());
        updateUser.setPassword(user.getPassword());
        updateUser.setBloodGroup(user.getBloodGroup());
        updateUser.setEmergencyContact1(user.getEmergencyContact1());
        updateUser.setEmergencyContact2(user.getEmergencyContact2());
        updateUser.setStatus(user.getStatus());
        updateUser.setCountryId(user.getCountryId());
        updateUser.setStateId(user.getStateId());
        updateUser.setDistrictId(user.getDistrictId());
        updateUser.setUpdatedBy(user.getUpdatedBy());
        updateUser.setUpdatedOn(user.getUpdatedOn());
        return userRepository.save(updateUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRoleRepository.deleteByUserId(id);
        userVehicleRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findFirstByEmail(email);
        if (user.isEmpty()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "user");
        }
        return user.get();
    }

    @Override
    public User findUserByMobile(String mobile) {
        Optional<User> user = userRepository.findFirstByMobile(mobile);
        if (user.isEmpty()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "user");
        }
        return user.get();
    }

    //for Auth And JWT Token
    @Override
    public UserDetailsDao getUserDetails(String email) {
        return computeUserDetails(findUserByEmail(email));
    }
    @Override
    public UserDetailsDao getUserDetailsById(Long id) {
        return computeUserDetails(getUserById(id));
    }

    private UserDetailsDao computeUserDetails(User user) {
        List<Role> roles = userRoleRepository.findAllRoleByUserId(user.getId());
        Set<String> rolesSet = roles.stream().map(Role::getRoleCode).collect(Collectors.toSet());
        Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
        List<Permission> permissions = permissionService.findAllPermissionByRoleIsIn(roleIds);
        Set<String> permissionSet = permissions.stream().map(Permission::getPermissionCode).collect(Collectors.toSet());
        return UserDetailsDao
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .password(user.getPassword())
                .roles(rolesSet)
                .permissions(permissionSet)
                .build();
    }
}
