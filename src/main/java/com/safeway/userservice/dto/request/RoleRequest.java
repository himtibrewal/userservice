package com.safeway.userservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class RoleRequest {

    @JsonProperty("role_name")
    @NotBlank(message = "Role Name is mandatory")
    private String roleName;

    @JsonProperty("role_code")
    @NotBlank(message = "Role code is mandatory")
    private String roleCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("permission_id")
    private List<Long> permissionId;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(List<Long> permissionId) {
        this.permissionId = permissionId;
    }
}
