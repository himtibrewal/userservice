package com.safeway.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {

    @JsonProperty("response_data")
    private T data;

    @JsonProperty("response_code")
    private String responseCode;

    @JsonProperty("response_message")
    private String responseMessage;

    @JsonProperty("response_status")
    private Integer responseStatus;

    @JsonProperty("current_page")
    private Integer currentPage;

    @JsonProperty("total_items")
    private Long totalItems;

    @JsonProperty("total_pages")
    private Integer totalPages;

}
