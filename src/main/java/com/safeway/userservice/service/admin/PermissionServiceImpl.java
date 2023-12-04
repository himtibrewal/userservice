package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.Permission;
import com.safeway.userservice.repository.admin.PermissionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public List<Permission> getAllPermission() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission updatePermission(Long id, Permission p) {
        Permission updatePermission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not exist with id: " + id));
        updatePermission.setPermissionName(p.getPermissionName());
        updatePermission.setPermissionCode(p.getPermissionCode());
        updatePermission.setDescription(p.getDescription());
        updatePermission.setUpdatedOn(LocalDateTime.now());
        return permissionRepository.save(updatePermission);
    }

    @Override
    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }
}
