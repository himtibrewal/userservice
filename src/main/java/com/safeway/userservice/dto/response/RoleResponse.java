package com.safeway.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safeway.userservice.entity.Permission;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RoleResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("role_code")
    private String roleCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("permissions")
    private List<Permission> permissionList;
}
