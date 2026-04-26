package com.nkh.ecommercebackend.exception;

import com.nkh.ecommercebackend.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException){
        String key = methodArgumentNotValidException.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(key);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(BaseResponse.error(errorCode));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<?>> handleBusinessException(BusinessException businessException) {
        ErrorCode errorCode = businessException.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(BaseResponse.error(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleException(Exception exception) {
        log.error("Unhandled exception: ", exception);
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(BaseResponse.error(errorCode));
    }
}
