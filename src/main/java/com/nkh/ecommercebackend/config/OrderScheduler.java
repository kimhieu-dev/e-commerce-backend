package com.nkh.ecommercebackend.config;

import com.nkh.ecommercebackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {
    private final OrderService orderService;

    @Scheduled(cron = "*/5 * * * * *")
    public void sendMessage() {
        log.info("start scheduling job");
        orderService.sendMail();
    }
}
