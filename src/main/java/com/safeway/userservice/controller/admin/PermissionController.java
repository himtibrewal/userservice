package com.safeway.userservice.controller;

import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.entity.admin.Permission;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.admin.PermissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permission")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> savePermission(@Valid @RequestBody Permission permission) {
        Long userID = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        permission.setCreatedBy(userID);
        permission.setUpdatedBy(userID);
        permission.setCreatedOn(LocalDateTime.now());
        permission.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(permissionService.savePermission(permission),
                "SF-201",
                "Permission Created Successfully",
                HttpStatus.CREATED.value()));
    }

    @GetMapping("/permission")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllPermission() {
        List<Permission> permissions = permissionService.getAllPermission();
        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<Permission>>(permissions,
                "SF-200",
                "Permission Found Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPermissionById(@PathVariable Long id) {
        Permission permission = permissionService.getPermissionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Permission>(permission,
                "SF-200",
                "Permission Found Successfully",
                HttpStatus.OK.value()));
    }

    @PutMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updatePermission(@PathVariable("id") Long id, @RequestBody Permission permission) {
        Long userID = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        permission.setUpdatedBy(userID);
        permission.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Permission>(permissionService.updatePermission(id, permission),
                "SF-200",
                "Permission Updated Successfully",
                HttpStatus.OK.value()));
    }

    @DeleteMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null,
                "SF-204",
                "Permission Deleted Successfully",
                HttpStatus.OK.value()));
    }
}
