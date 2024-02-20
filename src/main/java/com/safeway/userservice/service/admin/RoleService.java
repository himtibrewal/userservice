package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.Role;

import java.util.List;
import java.util.Set;

public interface RolesService {

    Role getRoleDTOById(Long id);

    Role getRoleById(Long id);

    Set<Role> findAllByIdInOrderById(List<Long> ids);

    List<Role> getAllRole();

    Role updateRole(Long id, Role role);

    Role saveRole(Role role);

    Role getRoleWithPermission(Long id);

    void deleteRole(Long id);
}

