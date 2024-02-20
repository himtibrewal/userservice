package com.safeway.userservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleRequest {

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("role_code")
    private String roleCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private Integer status = 1;

    @JsonProperty("permission_id")
    private List<Long> permissionIds;
}
