package com.safeway.userservice.controller;


import com.safeway.userservice.entity.admin.*;
import com.safeway.userservice.service.admin.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public Role saveRoles(@RequestBody Role role) {
        role.setCreatedBy(1);
        role.setUpdatedBy(1);
        return rolesService.saveRole(role);
    }

    @PutMapping("/roles/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Role updateRoles(@PathVariable("id") Long id, @RequestBody Role role) {
        role.setUpdatedBy(1);
        return rolesService.updateRole(id, role);
    }

    @GetMapping("/roles")
    @ResponseStatus(HttpStatus.OK)
    public List<Role> getAllRoles() {
        return rolesService.getAllRole();
    }

    @GetMapping("/roles/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Role> getRolesById(@PathVariable Long id) {
        return rolesService.getRoleById(id);
    }

    @DeleteMapping("/roles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoles(@PathVariable Long id) {
        rolesService.deleteRole(id);
    }

    // Permission
    @PostMapping("/permission")
    @ResponseStatus(HttpStatus.CREATED)
    public Permission savePermission(@RequestBody Permission permission) {
        permission.setCreatedBy(1);
        permission.setUpdatedBy(1);
        return permissionService.savePermission(permission);
    }

    @PutMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Permission updatePermission(@PathVariable("id") Long id, @RequestBody Permission permission) {
        permission.setUpdatedBy(1);
        return permissionService.updatePermission(id, permission);
    }

    @GetMapping("/permission")
    @ResponseStatus(HttpStatus.OK)
    public List<Permission> getAllPermission() {
        return permissionService.getAllPermission();
    }

    @GetMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Permission> getPermissionById(@PathVariable Long id) {
        return permissionService.getPermissionById(id);
    }

    @DeleteMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
    }

}
