package com.safeway.userservice.controller.admin;

import com.safeway.userservice.controller.BaseController;
import com.safeway.userservice.dto.response.PaginationResponse;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.entity.Permission;
import com.safeway.userservice.entity.Role;
import com.safeway.userservice.entity.RolePermission;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.admin.PermissionService;
import com.safeway.userservice.service.admin.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.safeway.userservice.utils.Commons.*;
import static com.safeway.userservice.utils.PermissionConstant.*;

@RestController
@RequestMapping("/admin")
public class PermissionController extends BaseController {
    private final PermissionService permissionService;
    private final RoleService roleService;

    @Autowired
    public PermissionController(PermissionService permissionService, RoleService roleService) {
        this.permissionService = permissionService;
        this.roleService = roleService;
    }

    @PostMapping("/permission")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> savePermission(@Valid @RequestBody Permission permission) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        checkAuthorizedUser(ADD_PERMISSION);
        permission.setCreatedBy(userId);
        permission.setUpdatedBy(userId);
        permission.setCreatedOn(LocalDateTime.now());
        permission.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Permission>(permissionService.savePermission(permission),
                "SF-200",
                "Permission Created Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/permission")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllPermission(
            @RequestParam(name = "paginated", defaultValue = PAGINATED_DEFAULT) boolean paginated,
            @RequestParam(name = "page", defaultValue = PAGE_O) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size,
            @RequestParam(name = "sort_by", defaultValue = SORT_BY_ID) String sortBy) {
        checkAuthorizedUser(GET_PERMISSION);
        if (paginated) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            Page<Permission> permissions = permissionService.getAllPermission(pageable);
            PaginationResponse<List<Permission>> response = PaginationResponse.<List<Permission>>builder()
                    .data(permissions.getContent())
                    .totalPages(permissions.getTotalPages())
                    .currentPage(permissions.getNumber())
                    .totalItems(permissions.getTotalElements())
                    .responseCode("SF-200")
                    .responseMessage("Permission Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            List<Permission> permissions = permissionService.getAllPermission();
            Response<List<Permission>> response = Response.<List<Permission>>builder()
                    .data(permissions)
                    .responseCode("SF-200")
                    .responseMessage("Permission Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @GetMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPermissionById(@PathVariable Long id) {
        checkAuthorizedUser(GET_PERMISSION);
        Permission permission = permissionService.getPermissionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Permission>(permission,
                "SF-200",
                "Permission Found Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/role/{id}/permission")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPermissionByRoleId(@PathVariable Long id) {
        checkAuthorizedUser(GET_PERMISSION);
        List<Permission> permissions = permissionService.getAllPermissionByRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<Permission>>(permissions,
                "SF-200",
                "Permission Found Successfully",
                HttpStatus.OK.value()));
    }

    @PutMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updatePermission(@PathVariable("id") Long id, @Valid @RequestBody Permission permission) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        checkAuthorizedUser(EDIT_PERMISSION);
        permission.setUpdatedBy(userId);
        permission.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Permission>(permissionService.updatePermission(id, permission),
                "SF-200",
                "Permission Updated Successfully",
                HttpStatus.OK.value()));
    }


    @PutMapping("/permission/{id}/role")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateRolePermission(@PathVariable("id") Long id, @RequestBody Set<Long> roleIds) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        checkAuthorizedUser(EDIT_PERMISSION);
        Set<Long> roleIdsByPermission = roleService.getAllRoleIdByPermissionId(id);

        Set<Long> insertMapping = roleIds
                .stream()
                .filter(e -> !roleIdsByPermission.contains(e))
                .collect(Collectors.toSet());
        Set<Long> deleteMapping = roleIdsByPermission
                .stream()
                .filter(e -> !roleIds.contains(e))
                .collect(Collectors.toSet());

        List<RolePermission> rolePermissions = insertMapping.stream().map(key -> {
            return RolePermission.builder()
                    .permission(Permission.builder().id(id).build())
                    .role(Role.builder().id(key).build())
                    .createdBy(userId)
                    .updatedBy(userId)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();
        }).collect(Collectors.toList());

        if(deleteMapping.size() > 0){
            permissionService.deletePermissionRoles(id, deleteMapping);
        }

        if(insertMapping.size() >0){
            permissionService.savePermissionRole(rolePermissions);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<RolePermission>>(List.of(),
                "SF-200",
                "Permission Updated Successfully",
                HttpStatus.OK.value()));
    }


    @DeleteMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deletePermission(@PathVariable Long id) {
        checkAuthorizedUser(DELETE_PERMISSION);
        permissionService.deletePermission(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null,
                "SF-204",
                "Permission Deleted Successfully",
                HttpStatus.OK.value()));
    }
}
