package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.Permission;
import com.safeway.userservice.entity.Role;
import com.safeway.userservice.entity.RolePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface RoleService {

    Role saveRole(Role role);
    Role getRoleById(Long id);
    List<Role> getAllRole();
    Page<Role> getAllRole(Pageable pageable);
    Set<Long> getAllRoleIdByPermissionId(Long permissionId);
    List<Role> getAllRoleByPermissionId(Long permissionId);
    Set<Long> getAllRoleIdByUserId(Long userId);
    List<Role> getAllRoleByUserId(Long userId);

    Role updateRole(Long id, Role role);
    List<RolePermission> saveRolePermission(List<RolePermission> rolePermissions);
    void deleteRolePermissions(Long roleId, Set<Long> permissionIds);
    void deleteRole(Long id);
    Set<Role> findAllByIdInOrderById(List<Long> ids);
}

