package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.Role;
import com.safeway.userservice.entity.RolePermission;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.RolePermissionRepository;
import com.safeway.userservice.repository.UserRoleRepository;
import com.safeway.userservice.repository.admin.RoleRepository;
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
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RolePermissionRepository rolePermissionRepository;

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository, UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Role getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (!role.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "Role");
        }
        return role.get();
    }







    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public Page<Role> getAllRole(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role updateRole(Long id, Role r) {
        Role role = getRoleById(id);
        role.setRoleName(r.getRoleName());
        role.setRoleCode(r.getRoleCode());
        role.setDescription(r.getDescription());
        role.setStatus(r.getStatus());
        role.setUpdatedBy(r.getUpdatedBy());
        role.setUpdatedOn(LocalDateTime.now());
        return roleRepository.save(role);
    }

    @Override
    public Set<Long> getAllRoleIdByPermissionId(Long permissionId) {
        return rolePermissionRepository.findAllRoleIdByPermissionId(permissionId);
    }

    @Override
    public List<Role> getAllRoleByPermissionId(Long permissionId) {
        return rolePermissionRepository.findAllRoleByPermissionId(permissionId);
    }

    @Override
    public Set<Long> getAllRoleIdByUserId(Long userId) {
        return userRoleRepository.findAllRoleIdByUserId(userId);
    }

    @Override
    public List<Role> getAllRoleByUserId(Long userId) {
        return userRoleRepository.findAllRoleByUserId(userId);
    }

    @Override
    public List<RolePermission> saveRolePermission(List<RolePermission> rolePermissions){
        return rolePermissionRepository.saveAll(rolePermissions);
    }

    @Override
    public void deleteRolePermissions(Long roleId, Set<Long> permissionIds){
         rolePermissionRepository.deleteByRoleIdAndPermissionIds(roleId, permissionIds);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        rolePermissionRepository.deleteByRoleId(id);
        userRoleRepository.deleteByRoleId(id);
        roleRepository.deleteById(id);
    }

    @Override
    public Set<Role> findAllByIdInOrderById(List<Long> ids) {
        Set<Role> roleList = roleRepository.findAllByIdInOrderById(ids);
        return roleList;
    }
}
