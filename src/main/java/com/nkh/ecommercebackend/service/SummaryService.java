package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.response.SummaryRes;
import com.nkh.ecommercebackend.entity.Carrier;
import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.Discount;
import com.nkh.ecommercebackend.entity.User;

public interface SummaryService {
    SummaryRes getSummary(Cart cart, Discount discount);
}
