package com.safeway.userservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "user_vehicle_mapping")
public class UserVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Basic(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.DETACH })
    @JoinColumns({ @JoinColumn(name = "user_id", referencedColumnName = "id") })
    private User user;

    @JsonIgnore
    @Basic(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.DETACH })
    @JoinColumns({ @JoinColumn(name = "vehicle_id", referencedColumnName = "id") })
    private Vehicle vehicle;

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
