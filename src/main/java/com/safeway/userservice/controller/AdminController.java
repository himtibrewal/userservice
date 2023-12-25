package com.safeway.userservice.controller;


import com.safeway.userservice.dto.request.RoleRequest;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.entity.admin.*;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.admin.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/admin")
public class AdminController {
    private final CountryService countryService;
    private final StateService stateService;
    private final DistrictService districtService;
    private final RolesService rolesService;
    private final PermissionService permissionService;

    public AdminController(CountryService countryService,
                           StateService stateService,
                           DistrictService districtService,
                           RolesService rolesService,
                           PermissionService permissionService) {
        this.countryService = countryService;
        this.stateService = stateService;
        this.districtService = districtService;
        this.rolesService = rolesService;
        this.permissionService = permissionService;
    }

    // Country Data Admin

    @PostMapping("/country")
    @ResponseStatus(HttpStatus.CREATED)
    public Country saveCountry(@RequestBody Country country) {
        country.setCreatedBy(1);
        country.setUpdatedBy(1);
        return countryService.saveCountry(country);
    }

    @PutMapping("/country/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Country updateCountry(@PathVariable("id") Long id, @RequestBody Country country) {
        country.setUpdatedBy(1);
        return countryService.updateCountry(id, country);
    }

    @GetMapping("/country")
    @ResponseStatus(HttpStatus.OK)
    public List<Country> getAllCountry() {
        return countryService.getAllCountry();
    }

    @GetMapping("/country/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Country> getCountryById(@PathVariable Long id) {
        return countryService.getCountryById(id);
    }

    @DeleteMapping("/country/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
    }

    //State Data Entry

    @PostMapping("/state")
    @ResponseStatus(HttpStatus.CREATED)
    public State saveState(@RequestBody State state) {
        state.setCreatedBy(1);
        state.setUpdatedBy(1);
        return stateService.saveState(state);
    }

    @PutMapping("/state/{id}")
    @ResponseStatus(HttpStatus.OK)
    public State updateState(@PathVariable("id") Long id, @RequestBody State state) {
        state.setUpdatedBy(1);
        return stateService.updateState(id, state);
    }

    @CrossOrigin(origins = "http://localhost:3003")
    @GetMapping("/state")
    @ResponseStatus(HttpStatus.OK)
    public List<State> getAllState() {
        return stateService.getAllState();
    }

    @GetMapping("country/{countryId}/state")
    @ResponseStatus(HttpStatus.OK)
    public List<State> getAllStateByCountryId(@PathVariable Long countryId) {
        return stateService.getStateByCountryId(countryId);
    }

    @GetMapping("/state/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<State> getStateById(@PathVariable Long id) {
        return stateService.getStateById(id);
    }

    @DeleteMapping("/state/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteState(@PathVariable Long id) {
        stateService.deleteState(id);
    }


    //District Data Entry

    @PostMapping("/district")
    @ResponseStatus(HttpStatus.CREATED)
    public District saveDistrict(@RequestBody District district) {
        district.setCreatedBy(1);
        district.setUpdatedBy(1);
        return districtService.saveDistrict(district);
    }

    @PutMapping("/district/{id}")
    @ResponseStatus(HttpStatus.OK)
    public District updateDistrict(@PathVariable("id") Long id, @RequestBody District district) {
        district.setUpdatedBy(1);
        return districtService.updateDistrict(id, district);
    }

    @GetMapping("/district")
    @ResponseStatus(HttpStatus.OK)
    public List<District> getAllDistrict() {
        return districtService.getAllDistrict();
    }

    @GetMapping("state/{stateId}/district")
    @ResponseStatus(HttpStatus.OK)
    public List<District> getAllDistrictByStateId(@PathVariable Long stateId) {
        return districtService.getDistrictByStateId(stateId);
    }

    @GetMapping("/district/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<District> getDistrictById(@PathVariable Long id) {
        return districtService.getDistrictById(id);
    }

    @DeleteMapping("/district/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDistrict(@PathVariable Long id) {
        districtService.deleteDistrict(id);
    }

    // Roles

    @PostMapping("/role")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveRole(@Valid @RequestBody RoleRequest roleRequest) {
        Long userID = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Set<Permission> permissionSet = new HashSet<>();
        if (roleRequest.getPermissionId() == null || !roleRequest.getPermissionId().isEmpty()) {
            permissionSet = permissionService.findAllByIdInOrderById(roleRequest.getPermissionId());
        }
        Role role = new Role(roleRequest.getRoleName(), roleRequest.getRoleCode(), roleRequest.getDescription(), permissionSet);
        role.setCreatedBy(userID);
        role.setUpdatedBy(userID);
        role.setCreatedOn(LocalDateTime.now());
        role.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(rolesService.saveRole(role),
                "SF-201",
                "ROLES Created Successfully",
                HttpStatus.CREATED.value()));
    }

    @PutMapping("/role/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateRoles(@PathVariable("id") Long id, @Valid @RequestBody RoleRequest roleRequest) {
        Long userID = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Set<Permission> permissionSet = new HashSet<>();
        if (roleRequest.getPermissionId() == null || !roleRequest.getPermissionId().isEmpty()) {
            permissionSet = permissionService.findAllByIdInOrderById(roleRequest.getPermissionId());
        }
        Role role = new Role(roleRequest.getRoleName(), roleRequest.getRoleCode(), roleRequest.getDescription(), permissionSet);
        role.setUpdatedBy(userID);
        role.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.ok(new Response<Role>(rolesService.updateRole(id, role), "roles Updated Successfully"));
    }

    @GetMapping("/roles")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(new Response<List<Role>>(rolesService.getAllRole(), "roles Found Successfully"));
    }

    @GetMapping("/role/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getRolesById(@PathVariable Long id) {
        return ResponseEntity.ok(new Response<Role>(rolesService.getRoleById(id), "roles Found Successfully"));
    }

    @DeleteMapping("/roles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoles(@PathVariable Long id) {
        rolesService.deleteRole(id);
    }

    // Permission
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

    @PutMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updatePermission(@PathVariable("id") Long id, @RequestBody Permission permission) {
        Long userID = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        permission.setUpdatedBy(userID);
        permission.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.ok(new Response<Permission>(permissionService.updatePermission(id, permission), "Permission Updated Successfully"));
    }

    @GetMapping("/permission")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllPermission() {
        return ResponseEntity.ok(new Response<List<Permission>>(permissionService.getAllPermission(), "Permission Found Successfully"));
    }

    @GetMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(new Response<Permission>(permissionService.getPermissionById(id), "Permission Found Successfully"));
    }

    @DeleteMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
    }

}
