package com.ebin.eatnow.services.dummy;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ebin.eatnow.dtos.PaymentDto;
import com.ebin.eatnow.services.PaymentService;

@Service
public class PaymentServiceDummy implements PaymentService {

    public PaymentDto pay(String orderId, double amount, String details) {

        UUID transactionId = UUID.randomUUID();
        PaymentDto dto = PaymentDto.builder()
                .transactionId(transactionId.toString())
                .orderId(orderId)
                .total(amount)
                .paymentDetails(details)
                .status(PaymentDto.PaymentStatus.SUCCESS.toString())
                .timeStamp(LocalDateTime.now())
                .build();
        return dto;
    }

    public PaymentDto get(String transactionId) {
        return null;
    }

    public boolean revert(String transactionId) {
        return true;
    }
}
