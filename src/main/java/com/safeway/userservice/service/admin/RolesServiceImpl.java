package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.Role;
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
    public Optional<Role> getRoleById(Long id) {
        return rolesRepository.findById(id);
    }

    @Override
    public List<Role> getAllRole() {
        return rolesRepository.findAll();
    }

    @Override
    public Role updateRole(Long id, Role r) {
        Role updateRole = rolesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not exist with id: " + id));
        updateRole.setRoleName(r.getRoleName());
        updateRole.setRoleCode(r.getRoleCode());
        updateRole.setDescription(r.getDescription());
        updateRole.setUpdatedOn(LocalDateTime.now());
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
