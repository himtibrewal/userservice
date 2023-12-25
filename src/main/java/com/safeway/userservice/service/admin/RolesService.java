package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.Role;

import java.util.List;
import java.util.Optional;

public interface RolesService {

    Role getRoleById(Long id);

    List<Role> getAllRole();

    Role updateRole(Long id, Role role);

    Role saveRole(Role role);

    void deleteRole(Long id);
}

