package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    Optional<Permission> getPermissionById(Long id);

    List<Permission> getAllPermission();

    Permission updatePermission(Long id, Permission permission);

    Permission savePermission(Permission permission);

    void deletePermission(Long id);

}
