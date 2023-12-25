package com.safeway.userservice.dto;

import com.safeway.userservice.entity.admin.Permission;
import com.safeway.userservice.entity.admin.Role;

import java.util.List;
import java.util.Set;

public class UserDetailsDao {
    private Long id;
    private String username;
    private String email;
    private String mobile;
    private String password;
    private String countryCode;
    private String emergency_contact1;
    private String emergency_contact2;
    private String bloodGroup;
    private Set<Role> roles;
    private Set<Permission> permissions;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmergency_contact1() {
        return emergency_contact1;
    }

    public void setEmergency_contact1(String emergency_contact1) {
        this.emergency_contact1 = emergency_contact1;
    }

    public String getEmergency_contact2() {
        return emergency_contact2;
    }

    public void setEmergency_contact2(String emergency_contact2) {
        this.emergency_contact2 = emergency_contact2;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
