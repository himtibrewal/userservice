package com.safeway.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StateResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("state_name")
    private String stateName;

    @JsonProperty("state_code")
    private String stateCode;

    @JsonProperty("state_abbr")
    private String stateAbbr;

    @JsonProperty("status")
    private Integer status = 1;

    @JsonProperty("country_id")
    private Long countryId;

    @JsonProperty("country_name")
    private String countryName;
}
