package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.TrackingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackingLogRepo extends JpaRepository<TrackingLog,String> {
    List<TrackingLog> findAllByOrderId(String orderId);
}
