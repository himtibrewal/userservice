package com.safeway.userservice.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse<T> {

    @JsonProperty("response_data")
    private T data;

    @JsonProperty("response_code")
    private String responseCode;

    @JsonProperty("response_message")
    private String responseMessage;

    @JsonProperty("response_status")
    private Integer responseStatus;

    public ErrorResponse(T data, String responseCode, String responseMessage, Integer responseStatus) {
        this.data = data;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseStatus = responseStatus;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    // Getter methods
}