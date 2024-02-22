package com.safeway.userservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("registration_no")
    private String registrationNo;

    @JsonProperty("type")
    private String type;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("image")
    private String image;

    @JsonProperty("model")
    private String model;

    @JsonProperty("status")
    private Integer status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle", cascade = CascadeType.PERSIST)
    private Set<UserVehicle> userVehicles;

    @JsonProperty("created_by")
    private Long createdBy;

    @JsonProperty("updated_by")
    private Long updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on", insertable = false)
    private LocalDateTime updatedOn;
}
