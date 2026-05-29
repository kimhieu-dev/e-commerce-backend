package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.dto.response.OrderSummary;
import com.nkh.ecommercebackend.entity.Address;
import com.nkh.ecommercebackend.entity.Discount;
import com.nkh.ecommercebackend.entity.Product;
import com.nkh.ecommercebackend.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GenerateOrderReq {
    User user;
    List<Product> products;
    Map<String, Integer> productQuantityMap;
    Discount discount;
    Address address;
    PaymentMethod paymentMethod;
    OrderSummary summary;
}
