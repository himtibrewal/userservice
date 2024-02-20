package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.Permission;
import com.safeway.userservice.entity.RolePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    Permission getPermissionById(Long id);

    List<Permission> getAllPermission();

    Page<Permission> getAllPermission(Pageable pageable);

    Set<Long> getAllPermissionIdByRole(Long roleId);

    List<Permission> getAllPermissionByRole(Long roleId);

    List<Permission> findAllPermissionByRoleIsIn(Set<Long> roleIds);

    Permission updatePermission(Long id, Permission permission);

    Permission savePermission(Permission permission);

    List<RolePermission> savePermissionRole(List<RolePermission> rolePermissions);

    void deletePermission(Long id);

    void deletePermissionRoles(Long permissionId, Set<Long> roleIds);

}
