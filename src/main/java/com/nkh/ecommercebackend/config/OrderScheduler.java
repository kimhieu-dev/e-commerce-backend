package com.nkh.ecommercebackend.config;

import com.nkh.ecommercebackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderScheduler {
    private final OrderService orderService;

    @Scheduled(cron = "0 */5 * * * *")
    public void sendMessage() {
        orderService.sendMail();
    }
}
