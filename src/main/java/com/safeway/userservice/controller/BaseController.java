package com.safeway.userservice.controller;

import com.safeway.userservice.dto.ErrorResponse;
import com.safeway.userservice.exception.ErrorEnum;

public class BaseController {

    public ErrorResponse getErrorResponse(ErrorEnum errorEnum) {
        return new ErrorResponse(errorEnum.getErrorCode(), errorEnum.getMessage(), errorEnum.getHttpStatus());
    }

}
