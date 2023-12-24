package com.safeway.userservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//    @ExceptionHandler(BusinessException.class)
//    ResponseEntity<ErrorResponse> processBusinessException(BusinessException ex) {
//        Exception exception = ex.originalException ?: ex
//        generateErrorResponse(ex.errorCode, ex.message, exception)
//    }
//
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    ResponseEntity processMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
//        return new ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED)
//    }
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    ResponseEntity processExpiredJwtException(ExpiredJwtException ex) {
//        generateErrorResponse(ErrorCode.INVALID_TOKEN, "Authorization header is invalid", ex)
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    ResponseEntity processMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        generateErrorResponse(ErrorCode.INVALID_REQUEST, ex.bindingResult.fieldError.field + " " + ex.bindingResult.fieldError.defaultMessage, ex)
//    }

    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
        return generateErrorResponse(ErrorEnum.ERROR_INTERNAL_SERVER_ERROR, "Something went wrong", ex);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<ErrorResponse> processBusinessException(Exception ex) {
        return generateErrorResponse(ErrorEnum.ERROR_INTERNAL_SERVER_ERROR, "Something went wrong", ex);
    }

    private ResponseEntity<ErrorResponse> generateErrorResponse(ErrorEnum errorCode, String message, Exception ex) {
       // logger.error("Exception Stack", ex);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getHttpStatus().value(), message);
        return new ResponseEntity<ErrorResponse>(errorResponse, errorCode.getHttpStatus());
    }
}