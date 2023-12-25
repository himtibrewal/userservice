package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.Permission;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionService {
    Permission getPermissionById(Long id);

    List<Permission> getAllPermission();

    Set<Permission> findAllByIdInOrderById(List<Long> ids);

    List<Permission> findAllByPermissionCodeInOrderById(List<String> permissionCode);


    Permission updatePermission(Long id, Permission permission);

    Permission savePermission(Permission permission);

    void deletePermission(Long id);

}
