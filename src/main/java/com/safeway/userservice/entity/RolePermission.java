package com.safeway.userservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "role_permission_mapping",
        uniqueConstraints= @UniqueConstraint(columnNames={"role_id", "permission_id"}))
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.DETACH })
    @JoinColumns({ @JoinColumn(name = "role_id", referencedColumnName = "id") })
    private Role role;

    @Basic(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.DETACH })
    @JoinColumns({ @JoinColumn(name = "permission_id", referencedColumnName = "id") })
    private Permission permission;

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
