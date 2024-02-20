package com.safeway.userservice.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private Set<String> roles;
    private Set<String> permissions;

}
