package com.safeway.userservice.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("role_name")
    @NotBlank(message = "Role Name is mandatory")
    private String roleName;

    @JsonProperty("role_code")
    @NotBlank(message = "Role code is mandatory")
    private String roleCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    @Column(name = "status")
    private Integer status = 1;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private Set<UserRole> userRoles;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private Set<RolePermission> rolePermissions;

    @JsonIgnore
    @JsonProperty("created_by")
    private Long createdBy;

    @JsonIgnore
    @JsonProperty("updated_by")
    private Long updatedBy;

    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on", insertable = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdOn;

    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on", insertable = true)
    private LocalDateTime updatedOn;
}