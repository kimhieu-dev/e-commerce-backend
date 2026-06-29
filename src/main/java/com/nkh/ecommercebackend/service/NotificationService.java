package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.entity.Order;

public interface NotificationService {
    void sendMail(String email,String trackingNumber);
}
