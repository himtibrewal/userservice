package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.Role;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.admin.RolesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RolesServiceImpl implements RolesService {

    private final RolesRepository rolesRepository;

    public RolesServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public Role getRoleById(Long id) {
        Optional<Role> role = rolesRepository.findById(id);
        if (!role.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "roles");
        }
        return role.get();

    }

    @Override
    public List<Role> getAllRole() {
        List<Role> roleList = rolesRepository.findAll();
        if (roleList.isEmpty()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "roles");
        }
        return roleList;
    }

    @Override
    public Role updateRole(Long id, Role r) {
        Optional<Role> role = rolesRepository.findById(id);
        if (!role.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "Roles");
        }
        Role updateRole = role.get();
        updateRole.setRoleName(r.getRoleName());
        updateRole.setRoleCode(r.getRoleCode());
        updateRole.setDescription(r.getDescription());
        updateRole.setUpdatedOn(LocalDateTime.now());
        updateRole.setPermissions(r.getPermissions());
        return rolesRepository.save(updateRole);
    }

    @Override
    public Role saveRole(Role role) {
        return rolesRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        rolesRepository.deleteById(id);
    }
}
