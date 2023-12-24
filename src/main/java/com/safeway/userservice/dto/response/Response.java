package com.safeway.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response<T> {

    @JsonProperty("response_data")
    private T data;

    @JsonProperty("response_code")
    private String responseCode;

    @JsonProperty("response_message")
    private String responseMessage;

    @JsonProperty("response_status")
    private String responseStatus;

    public Response(T data, String responseCode, String responseMessage, String responseStatus) {
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

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }
}
