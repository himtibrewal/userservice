package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.Permission;
import com.safeway.userservice.entity.admin.Role;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.admin.PermissionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission getPermissionById(Long id) {
        Optional<Permission> permission = permissionRepository.findById(id);
        if (!permission.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "roles");
        }
        return permission.get();
    }

    @Override
    public Set<Permission> findAllByIdInOrderById(List<Long> ids) {
        Set<Permission> permissionList = permissionRepository.findAllByIdInOrderById(ids);
        return permissionList;
    }

    @Override
    public List<Permission> findAllByPermissionCodeInOrderById(List<String> permissionCode) {
        List<Permission> permissionList = permissionRepository.findAllByPermissionCodeInOrderById(permissionCode);
        return permissionList;
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
