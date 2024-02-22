package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.Permission;
import com.safeway.userservice.entity.RolePermission;
import com.safeway.userservice.entity.admin.Country;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.RolePermissionRepository;
import com.safeway.userservice.repository.admin.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    private final RolePermissionRepository rolePermissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public Permission getPermissionById(Long id) {
        Optional<Permission> permission =  permissionRepository.findById(id);
        if(!permission.isPresent()){
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "Permission");
        }
        return permission.get();

    }

    @Override
    public List<Permission> getAllPermission() {
        return permissionRepository.findAll();
    }

    @Override
    public Page<Permission> getAllPermission(Pageable pageable) {
        return permissionRepository.findAll(pageable);
    }

    @Override
    public Set<Long> getAllPermissionIdByRole(Long roleId) {
        return rolePermissionRepository.findAllPermissionIdByRoleId(roleId);
    }
    @Override
    public List<Permission> getAllPermissionByRole(Long roleId) {
        return rolePermissionRepository.findAllPermissionByRoleId(roleId);
    }

    @Override
    public List<Permission> findAllPermissionByRoleIsIn(Set<Long> roleIds) {
        return rolePermissionRepository.findAllPermissionByRoleIsIn(roleIds);
    }

    @Override
    public Permission updatePermission(Long id, Permission p) {
        Permission permission =  getPermissionById(id);
        permission.setPermissionName(p.getPermissionName());
        permission.setPermissionCode(p.getPermissionCode());
        permission.setDescription(p.getDescription());
        permission.setStatus(p.getStatus());
        permission.setUpdatedBy(p.getUpdatedBy());
        permission.setUpdatedOn(LocalDateTime.now());
        return permissionRepository.save(permission);
    }

    @Override
    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public List<RolePermission> savePermissionRole(List<RolePermission> rolePermissions){
        return rolePermissionRepository.saveAll(rolePermissions);
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        rolePermissionRepository.deleteByPermissionId(id);
        permissionRepository.deleteById(id);
    }

    @Override
    public void deletePermissionRoles(Long permissionId, Set<Long> roleIds) {
        rolePermissionRepository.deleteByPermissionIdAndRoleIds(permissionId, roleIds);
    }
}
