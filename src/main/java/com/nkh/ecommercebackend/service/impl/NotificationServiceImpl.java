package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.entity.Order;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender mailSender;

    @Override
    public void sendMail(Order order) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("nguyenkimhieu.dev@gmail.com");
            message.setTo(order.getUser().getEmail());
            message.setSubject("Confirmed: Order #" + order.getId() + " deliver succeed!");
            message.setText("Hi,\n\nYour order is delivered. Thank for purchasing!");
            mailSender.send(message);
            log.info("Send mail success for order: {}", order.getId());
        }catch (Exception e){
            log.error("Error sending mail: {}: {}", order.getId(), e.getMessage());
            throw new BusinessException(ErrorCode.SEND_MAIL_FAIL);
        }
    }
}
