package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.Role;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.admin.RoleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RolesServiceImpl implements RolesService {

    private final RoleRepository roleRepository;

    public RolesServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (!role.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "roles");
        }
        return role.get();
    }

    @Override
    public Role getRoleDTOById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (!role.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "roles");
        }
        return Role.builder()
                .id(role.get().getId())
                .roleName(role.get().getRoleName())
                .roleCode(role.get().getRoleCode())
                .status(role.get().getStatus())
                .description(role.get().getDescription())
               // .permissionId(role.get().getPermissions().stream().map(Permission::getId).collect(Collectors.toSet()))
                .build() ;
    }

    @Override
    public Role getRoleWithPermission(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (!role.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "roles");
        }
        return role.get();
    }

    @Override
    public List<Role> getAllRole() {
        List<Role> roleList = roleRepository.findAll();
        if (roleList.isEmpty()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "roles");
        }
        return roleList;
    }

    @Override
    public Set<Role> findAllByIdInOrderById(List<Long> ids) {
        Set<Role> roleList = roleRepository.findAllByIdInOrderById(ids);
        return roleList;
    }

    @Override
    public Role updateRole(Long id, Role r) {
        Optional<Role> role = roleRepository.findById(id);
        if (!role.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "Roles");
        }
        Role updateRole = role.get();
        updateRole.setRoleName(r.getRoleName());
        updateRole.setRoleCode(r.getRoleCode());
        updateRole.setDescription(r.getDescription());
        updateRole.setUpdatedOn(LocalDateTime.now());
        //updateRole.setPermissions(r.getPermissions());
        return roleRepository.save(updateRole);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
