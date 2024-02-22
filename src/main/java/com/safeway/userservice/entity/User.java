package com.safeway.userservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`username`", nullable = false)
    private String username;

    @Column(name = "`email`", nullable = false, unique = true)
    private String email;

    @Column(name = "`mobile`", nullable = false, unique = true)
    private String mobile;

    @JsonIgnore
    @Column(name = "`password`", nullable = false)
    private String password;

    @JsonProperty("emergency_contact1")
    private String emergencyContact1;

    @JsonProperty("emergency_contact2")
    private String emergencyContact2;

    @JsonProperty("blood_group")
    private String bloodGroup;

    @JsonProperty("status")
    @Column(name = "status")
    private Integer status = 1;

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

    @JsonProperty("lat")
    private Double lat;

    @JsonProperty("lon")
    private Double lon;

    @JsonProperty("reg_key")
    private String regKey;

    @JsonProperty("device_key")
    private String deviceKey;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<UserRole> userRoles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<UserVehicle> userVehicles;

    @JsonIgnore
    @JsonProperty("created_by")
    private Long createdBy = id;

    @JsonIgnore
    @JsonProperty("updated_by")
    private Long updatedBy = id;

    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on", insertable = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdOn;

    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on", insertable = true, updatable = true)
    private LocalDateTime updatedOn;

}
