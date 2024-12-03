package com.hust.Ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hust.Ecommerce.constants.MessageKeys;
import com.hust.Ecommerce.dtos.ApiResponse;
import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.exceptions.payload.ExpiredTokenException;
import com.hust.Ecommerce.exceptions.payload.InvalidParamException;
import com.hust.Ecommerce.exceptions.payload.RefreshTokenException;
import com.hust.Ecommerce.exceptions.payload.VerificationException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // xu ly ngoai le lien quan du lieu
    @ExceptionHandler(value = {
            ResourceNotFoundException.class,
            InvalidParamException.class
    })
    public ResponseEntity<ApiResponse> handleSpecificExceptions(Exception e) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        String detail = MessageKeys.ERROR_MESSAGE;

        if (e instanceof ResourceNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            detail = e.getMessage();
        } else if (e instanceof InvalidParamException) {
            status = HttpStatus.BAD_REQUEST;
            detail = e.getMessage();
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(String.valueOf(status.value()));
        apiResponse.setError(detail);
        return ResponseEntity.status(status).body(apiResponse);
    }

    // xu ly ngoai le authenticated
    @ExceptionHandler({ AuthenticationException.class, VerificationException.class, ExpiredTokenException.class })

    public ResponseEntity<ApiResponse> authenticationException(Exception e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String detail = e.getMessage();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(String.valueOf(status.value()));
        apiResponse.setError(detail);
        return ResponseEntity.status(status).body(apiResponse);
    }

    // xu ly ngoai le runtime
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<Void>> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);
        ApiResponse<Void> apiResponse = new ApiResponse<Void>();

        apiResponse.setMessage(String.valueOf(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode()));
        apiResponse.setError(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // xu ly ngoai le appException
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setMessage(String.valueOf(errorCode.getCode()));
        apiResponse.setError(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    // xu ly ngoai le Tu choi truy cap
    @ExceptionHandler({ AccessDeniedException.class, RefreshTokenException.class })
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(String.valueOf(errorCode.getCode()))
                        .error(errorCode.getMessage())
                        .build());
    }

}
