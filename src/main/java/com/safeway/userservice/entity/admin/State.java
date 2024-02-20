package com.safeway.userservice.entity.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "state")
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("state_name")
    private String stateName;

    @JsonProperty("state_code")
    private String stateCode;

    @JsonProperty("state_abbr")
    private String stateAbbr;

    @JsonProperty("status")
    private Integer status = 1;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    private Country country;

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
    @Column(name = "updated_on", insertable = false, updatable = true)
    private LocalDateTime updatedOn;
}

