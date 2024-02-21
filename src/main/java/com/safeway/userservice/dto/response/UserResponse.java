package com.safeway.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safeway.userservice.entity.Role;
import com.safeway.userservice.entity.Vehicle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@Builder
public class UserResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("password")
    private String password;

    @JsonProperty("country_id")
    private Long countryId;

    @JsonProperty("state_id")
    private Long stateId;

    @JsonProperty("district_id")
    private Long districtId;

    @JsonProperty("address1")
    private String address1;

    @JsonProperty("address2")
    private String address2;

    @JsonProperty("emergency_contact1")
    private String emergencyContact1;

    @JsonProperty("emergency_contact2")
    private String emergencyContact2;

    @JsonProperty("blood_group")
    private String bloodGroup;

    @JsonProperty("status")
    private Integer status = 1;

    @JsonProperty("roles")
    private List<Role> roles;

    @JsonProperty("vehicles")
    private List<Vehicle> vehicles;
}
