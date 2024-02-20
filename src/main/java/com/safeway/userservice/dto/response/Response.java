package com.safeway.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    @JsonProperty("response_data")
    private T data;

    @JsonProperty("response_code")
    private String responseCode;

    @JsonProperty("response_message")
    private String responseMessage;

    @JsonProperty("response_status")
    private Integer responseStatus;
}