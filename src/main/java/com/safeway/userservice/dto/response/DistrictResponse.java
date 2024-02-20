package com.safeway.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DistrictResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("district_name")
    private String districtName;

    @JsonProperty("district_code")
    private String districtCode;

    @JsonProperty("district_abbr")
    private String districtAbbr;

    @JsonProperty("status")
    private Integer status = 1;

    @JsonProperty("state_id")
    private Long stateId;

    @JsonProperty("state_name")
    private String stateName;

    @JsonProperty("country_id")
    private Long countryId;

    @JsonProperty("country_name")
    private String countryName;
}
