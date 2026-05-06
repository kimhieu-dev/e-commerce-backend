package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.response.SummaryRes;

public interface SummaryService {
    SummaryRes getSummary(String discountCode);
}
