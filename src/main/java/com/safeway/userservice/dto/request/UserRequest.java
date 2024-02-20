package com.safeway.userservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRequest {

    @JsonProperty("username")
    @NotBlank(message = "Name is mandatory")
    private String userName;

    @JsonProperty("email")
    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    @NotBlank(message = "Mobile is mandatory")
    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("password")
    private String password;

    @JsonProperty("country_code")
    private String countryCode;

    private Long countryId;

    private Long stateId;

    private Long cityId;

    private String address1;

    private String address2;

    @JsonProperty("emergency_contact1")
    private String emergencyContact1;

    @JsonProperty("emergency_contact2")
    private String emergencyContact2;

    @JsonProperty("blood_group")
    private String bloodGroup;

    @JsonProperty("status")
    private Integer status = 1;

    @JsonProperty("role_ids")
    @Size(min = 1)
    private List<Long> roleIds;

}
