package com.eatnow.order.services.dummy;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.eatnow.order.dtos.Payment;
import com.eatnow.order.services.PaymentService;

@Service
public class PaymentServiceDummy implements PaymentService {

    public Payment pay(String orderId, double amount, String details) {

        UUID transactionId = UUID.randomUUID();
        Payment dto = Payment.builder()
                .transactionId(transactionId.toString())
                .orderId(orderId)
                .total(amount)
                .paymentDetails(details)
                .status(Payment.PaymentStatus.SUCCESS.toString())
                .timeStamp(LocalDateTime.now())
                .build();
        return dto;
    }

    public Payment get(String transactionId) {
        return null;
    }

    public boolean revert(String transactionId) {
        return true;
    }
}
