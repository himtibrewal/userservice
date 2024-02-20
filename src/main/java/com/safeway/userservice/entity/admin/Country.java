package com.safeway.userservice.entity.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

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
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("country_name")
    @NotNull(message = "Name is mandatory")
    private String countryName;

    @JsonProperty("country_code")
    @NotNull(message = "Code is mandatory")
    @Positive(message = "Only Positive Number Allowed")
    private Integer countryCode;

    @JsonProperty("country_abbr")
    @NotNull(message = "Abbr is mandatory")
    private String countryAbbr;

    @JsonProperty("status")
    private Integer status = 1;

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
    private LocalDateTime createdOn ;

    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on", insertable = true, updatable = true)
    private LocalDateTime updatedOn;

}
