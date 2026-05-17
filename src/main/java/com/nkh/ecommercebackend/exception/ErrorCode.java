package com.nkh.ecommercebackend.exception;

import lombok.Getter;
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
    USER_ID_NOT_FOUND(2019, "User id not found", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND(2020, "Cart not found", HttpStatus.NOT_FOUND),
    USER_DOES_NOT_HAVE_CART(2021, "User does not have a cart", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(2022, "Product not found", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(2023, "Cart item not found", HttpStatus.NOT_FOUND),
    //PRODUCT
    SKU_BLANK(2024, "SKU is blank", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_BLANK(2025, "Product name is blank", HttpStatus.BAD_REQUEST),
    BASE_PRICE_NULL(2026, "Base price is null", HttpStatus.BAD_REQUEST),
    BASE_PRICE_INVALID(2027, "Base price is invalid", HttpStatus.BAD_REQUEST),
    WEIGHT_NULL(2028, "Weight is null", HttpStatus.BAD_REQUEST),
    WEIGHT_INVALID(2029, "Weight is invalid", HttpStatus.BAD_REQUEST),
    LENGTH_NULL(2030, "Length is null", HttpStatus.BAD_REQUEST),
    LENGTH_INVALID(2031, "Length is invalid", HttpStatus.BAD_REQUEST),
    WIDTH_NULL(2032, "Width is null", HttpStatus.BAD_REQUEST),
    WIDTH_INVALID(2033, "Width is invalid", HttpStatus.BAD_REQUEST),
    HEIGHT_NULL(2034, "Height is null", HttpStatus.BAD_REQUEST),
    HEIGHT_INVALID(2035, "Height is invalid", HttpStatus.BAD_REQUEST),
    QUANTITY_IN_STOCK_NULL(2036, "Quantity in stock is null", HttpStatus.BAD_REQUEST),
    RESERVED_QUANTITY_NULL(2037, "Reserved quantity is null", HttpStatus.BAD_REQUEST),
    SKU_EXISTED(2038, "SKU is already existed", HttpStatus.CONFLICT),
    PRODUCT_DO_NOT_HAVE_INVENTORY(2039, "Product does not have a inventory", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK(2040, "Product out of stock", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_RANGE(2041, "Product out of range", HttpStatus.BAD_REQUEST),
    CART_ID_BLANK(2042, "Cart id is blank", HttpStatus.BAD_REQUEST),
    PRODUCT_ID_BLANK(2043, "Product id is blank", HttpStatus.BAD_REQUEST),
    QUANTITY_INVALID(2044, "Quantity is invalid", HttpStatus.BAD_REQUEST),
    DISCOUNT_NOT_FOUND(2045, "Discount not found", HttpStatus.NOT_FOUND),
    DISCOUNT_EXPIRED(2046, "Discount expired", HttpStatus.BAD_REQUEST),
    PAYMENT_METHOD_NULL(2047, "Payment method is null", HttpStatus.BAD_REQUEST),
    DISCOUNT_CODE_BLANK(2048, "Discount code is blank", HttpStatus.BAD_REQUEST),
    CARRIER_BLANK(2049, "Carrier is blank", HttpStatus.BAD_REQUEST),
    ADDRESS_BLANK(2050, "Address is blank", HttpStatus.BAD_REQUEST),
    CARRIER_NOT_FOUND(2051, "Carrier not found", HttpStatus.NOT_FOUND),
    ADDRESS_NOT_FOUND(2052, "Address not found", HttpStatus.NOT_FOUND),
    PAYMENT_METHOD_INVALID(2053, "Payment method is invalid", HttpStatus.BAD_REQUEST),
    DISCOUNT_EXCEED(2054, "Discount exceeded", HttpStatus.BAD_REQUEST),
    CART_IS_EMPTY(2055, "Cart is empty", HttpStatus.BAD_REQUEST),
    USER_DOES_NOT_HAVE_PRIVILEGE(2056, "User does not have privilege", HttpStatus.FORBIDDEN),
    ORDER_NOT_FOUND(2057, "Order does not exist", HttpStatus.NOT_FOUND),
    ORDER_ALREADY_CONFIRMED(2058, "Order already confirmed", HttpStatus.BAD_REQUEST),
    ORDER_AWAITING_PAYMENT(2059, "Order awaiting payment", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_REJECTED(2060, "Order already rejected", HttpStatus.BAD_REQUEST),
    RESERVED_COUNT_NEGATIVE(2061, "Reserved count is negative", HttpStatus.BAD_REQUEST),
    TRACKING_LOGS_NOT_FOUND(2062, "Tracking logs not found", HttpStatus.NOT_FOUND),
    YOU_DO_NOT_HAVE_PRIVILEGE(2063, "You do not have privilege", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_PICKING(2064, "Order already picking", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_SHIPPING(2065, "Order already shipping", HttpStatus.BAD_REQUEST),
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
