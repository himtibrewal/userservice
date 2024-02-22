package com.safeway.userservice.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ExpiredJwtException.class)
    ResponseEntity processExpiredJwtException(ExpiredJwtException ex) {
        return generateErrorResponse(null, ErrorEnum.ERROR_EXPIRED_JWT_TOKEN, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity processAuthenticationException(AuthenticationException ex) {
        logger.error("final error:: {}", ex.getMessage());
        return generateErrorResponse(null, ErrorEnum.ERROR_UNAUTHENTICATED, ex.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.error("final error:: {}", ex.getMessage());
        return generateErrorResponse(errors, ErrorEnum.ERROR_BAD_REQUEST, "Something went wrong");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnAuthorizeException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(UnAuthorizeException ex) {
        logger.error("final error:: {}", ex.getMessage());
        return generateErrorResponse(null, ErrorEnum.ERROR_FORBIDDEN, "User Not Authorized");
    }


    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ErrorResponse> processNotFoundException(NotFoundException ex) {
        logger.error("final error:: {}", ex.getMessage());
        return generateErrorResponse(ex.getMessage() + " " + ex.getErrorCode().getMessage(), ex.getErrorCode(), ex.getMessage());
    }

    ResponseEntity<ErrorResponse> processUserAlreadyExistException(UserAlreadyExistException ex) {
        logger.error("final error:: {}", ex.getMessage());
        return generateErrorResponse(null, ex.getErrorCode(), "Something went wrong");
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> processUnHandleException(Exception ex) {
        logger.error("final error:: {}", ex.getMessage());
        return generateErrorResponse(null, ErrorEnum.ERROR_INTERNAL_SERVER_ERROR, "Something went wrong");
    }

    private ResponseEntity<ErrorResponse> generateErrorResponse(Object data, ErrorEnum errorCode, String message) {
        ErrorResponse errorResponse = new ErrorResponse(data, errorCode.getErrorCode(), errorCode.getMessage(), errorCode.getHttpStatus().value());
        return new ResponseEntity<ErrorResponse>(errorResponse, errorCode.getHttpStatus());
    }
}