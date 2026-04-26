package com.nkh.ecommercebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nkh.ecommercebackend.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return BaseResponse.<T>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }
}
