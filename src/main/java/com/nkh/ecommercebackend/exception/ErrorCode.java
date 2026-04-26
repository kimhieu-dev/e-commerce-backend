package com.nkh.ecommercebackend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //AUTH / USER
    UNAUTHENTICATED(1001, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1002, "You do not have permission", HttpStatus.FORBIDDEN),
    USER_EXISTED(1003, "User already existed", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1004, "User not found", HttpStatus.NOT_FOUND),
    EMAIL_EXISTED(1005, "Email already existed", HttpStatus.CONFLICT),
    EMAIL_NOT_FOUND(1006, "Email not found", HttpStatus.NOT_FOUND),
    PHONE_NUMBER_EXISTED(1007, "Phone number already existed", HttpStatus.CONFLICT),
    PHONE_NUMBER_NOT_FOUND(1008, "Phone number not found", HttpStatus.NOT_FOUND),
    //VALIDATION
    //USER
    INVALID_REQUEST(2001, "Invalid request data", HttpStatus.BAD_REQUEST),
    USERNAME_BLANK(2002, "Username is blank", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(2003, "Username is invalid (from 8 to 12)", HttpStatus.BAD_REQUEST),
    PASSWORD_BLANK(2004, "Password is blank", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(2005, "Password is invalid", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(2006, "Email is invalid", HttpStatus.BAD_REQUEST),
    EMAIL_BLANK(2007, "Email is blank", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_INVALID(2008, "Phone number is invalid", HttpStatus.BAD_REQUEST),
    FULL_NAME_BLANK(2009, "Full name is blank", HttpStatus.BAD_REQUEST),
    FULL_NAME_TOO_LONG(2010, "Full name is too long (maximum: 100)", HttpStatus.BAD_REQUEST),
    GENDER_NULL(2011, "Gender is null", HttpStatus.BAD_REQUEST),
    DATE_BIRTH_INVALID(2012, "Date of birth is invalid", HttpStatus.BAD_REQUEST),
    ROLE_NULL(2013, "Role is null", HttpStatus.BAD_REQUEST),
    ROLE_INVALID(2014, "Role is invalid", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(2015, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(2016, "Role already existed", HttpStatus.CONFLICT),
    ROLE_NAME_BLANK(2017, "Role name is blank", HttpStatus.BAD_REQUEST),
    //CART
    USER_ID_NULL(2018, "User id is null", HttpStatus.BAD_REQUEST),
    //SYSTEM
    UNCATEGORIZED_ERROR(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;
    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
