package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.service.TrackingNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TrackingNumberGeneratorImpl implements TrackingNumberGenerator {
    @Override
    public String generateTrackingNumber(String prefix) {
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return prefix.toUpperCase() + uuid.substring(0, 8);
    }
}
