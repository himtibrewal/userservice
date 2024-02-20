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

@RestController
@RequestMapping("/admin")
public class RoleController extends BaseController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    public RoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @PostMapping("/role")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveRole(@Valid @RequestBody Role role) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        role.setCreatedBy(userId);
        role.setUpdatedBy(userId);
        role.setCreatedOn(LocalDateTime.now());
        role.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Role>(roleService.saveRole(role),
                "SF-200",
                "Role Created Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/role")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllRole(
            @RequestParam(name = "paginated", defaultValue = PAGINATED_DEFAULT) boolean paginated,
            @RequestParam(name = "page", defaultValue = PAGE_O) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size,
            @RequestParam(name = "sort_by", defaultValue = SORT_BY_ID) String sortBy) {
        if (paginated) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            Page<Role> roles = roleService.getAllRole(pageable);
            PaginationResponse<List<Role>> response = PaginationResponse.<List<Role>>builder()
                    .data(roles.getContent())
                    .totalPages(roles.getTotalPages())
                    .currentPage(roles.getNumber())
                    .totalItems(roles.getTotalElements())
                    .responseCode("SF-200")
                    .responseMessage("Role Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            List<Role> roles = roleService.getAllRole();
            Response<List<Role>> response = Response.<List<Role>>builder()
                    .data(roles)
                    .responseCode("SF-200")
                    .responseMessage("Role Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @GetMapping("/role/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Role>(role,
                "SF-200",
                "Role Found Successfully",
                HttpStatus.OK.value()));
    }


    @GetMapping("/permission/{id}/role")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPermissionByRoleId(@PathVariable Long id) {
        List<Role> roles = roleService.getAllRoleByPermissionId(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<Role>>(roles,
                "SF-200",
                "Role Found Successfully",
                HttpStatus.OK.value()));
    }

    @PutMapping("/role/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateRole(@PathVariable("id") Long id, @Valid @RequestBody Role role) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        role.setUpdatedBy(userId);
        role.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Role>(roleService.updateRole(id, role),
                "SF-200",
                "Role Updated Successfully",
                HttpStatus.OK.value()));
    }


    @PutMapping("/role/{id}/permission")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateRolePermission(@PathVariable("id") Long id, @RequestBody Set<Long> permissionIds) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Set<Long> permissionIdsByRole = permissionService.getAllPermissionIdByRole(id);

        Set<Long> insertMapping = permissionIds
                .stream()
                .filter(e -> !permissionIdsByRole.contains(e))
                .collect(Collectors.toSet());
        Set<Long> deleteMapping = permissionIdsByRole
                .stream()
                .filter(e -> !permissionIds.contains(e))
                .collect(Collectors.toSet());

        List<RolePermission> rolePermissions = insertMapping.stream().map(key -> {
           return RolePermission.builder()
                    .permission(Permission.builder().id(key).build())
                    .role(Role.builder().id(id).build())
                    .createdBy(userId)
                    .updatedBy(userId)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();
        }).collect(Collectors.toList());

        if(deleteMapping.size() > 0){
            roleService.deleteRolePermissions(id, deleteMapping);
        }

        if(insertMapping.size() >0){
            roleService.saveRolePermission(rolePermissions);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<RolePermission>>(List.of(),
                "SF-200",
                "Role Updated Successfully",
                HttpStatus.OK.value()));
    }

    @DeleteMapping("/role/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null,
                "SF-204",
                "Role Deleted Successfully",
                HttpStatus.OK.value()));
    }
}
