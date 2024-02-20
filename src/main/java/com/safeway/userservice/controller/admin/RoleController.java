package com.safeway.userservice.controller;

import com.safeway.userservice.dto.RoleDTO;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.entity.admin.Permission;
import com.safeway.userservice.entity.admin.Role;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.admin.PermissionService;
import com.safeway.userservice.service.admin.RolesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class RoleController {

    private final RolesService rolesService;

    private final PermissionService permissionService;

    public RoleController(RolesService rolesService, PermissionService permissionService) {
        this.rolesService = rolesService;
        this.permissionService = permissionService;
    }

    @PostMapping("/role")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveRole(@Valid @RequestBody RoleDTO roleDTO) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Set<Permission> permissionSet = Set.of();
//        Set<User> userList = Set.of();
        if (roleDTO.getPermissionId() != null && roleDTO.getPermissionId().size() > 0) {
            permissionSet = permissionService.getPermissionByIds(roleDTO.getPermissionId());
        }

//        if(roleDTO.getUserId() != null && roleDTO.getUserId().size() > 0){
//            userList = userService.getUserByIds(roleDTO.getUserId());
//        }

        Role role = Role.builder()
                .roleName(roleDTO.getRoleName())
                .roleCode(roleDTO.getRoleCode())
                .description(roleDTO.getDescription())
                .status(roleDTO.getStatus() == null ? 1 : roleDTO.getStatus())
                .permissions(permissionSet)
//                .users(userList)
                .createdBy(userId)
                .createdOn(LocalDateTime.now())
                .updatedBy(userId)
                .updatedOn(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Role>(rolesService.saveRole(role),
                "SF-201",
                "Role Created Successfully",
                HttpStatus.OK.value()));

    }

    @PutMapping("/role/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateRoles(@PathVariable("id") Long id, @Valid @RequestBody RoleDTO roleDTO) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Role role1 = rolesService.getRoleById(id);

        Set<Permission> permissionSet = Set.of();
        if (roleDTO.getPermissionId() != null && roleDTO.getPermissionId().size() > 0) {
            permissionSet = permissionService.getPermissionByIds(roleDTO.getPermissionId());
        }

        Role role = Role.builder()
                .id(id)
                .roleName(roleDTO.getRoleName())
                .roleCode(roleDTO.getRoleCode())
                .description(roleDTO.getDescription())
                .status(roleDTO.getStatus() == null ? 1 : roleDTO.getStatus())
                .permissions(permissionSet)
//                .users(userList)
                .createdBy(role1.getCreatedBy())
                .createdOn(role1.getCreatedOn())
                .updatedBy(userId)
                .updatedOn(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Role>(rolesService.saveRole(role),
                "SF-200",
                "Role Updated Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/roles")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllRoles() {
        List<Role> roles = rolesService.getAllRole();
        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<Role>>(roles,
                "SF-200",
                "Roles roles Found Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/role/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getRolesById(@PathVariable Long id) {
        Role role = rolesService.getRoleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Role>(role,
                "SF-200",
                "Role Found Successfully",
                HttpStatus.OK.value()));
    }

    @DeleteMapping("/role/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteRoles(@PathVariable Long id) {
        rolesService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null,
                "SF-204",
                "Role Deleted Successfully",
                HttpStatus.OK.value()));
    }
}
