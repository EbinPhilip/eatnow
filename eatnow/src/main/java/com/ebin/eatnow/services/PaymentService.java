package com.ebin.eatnow.services;

import com.ebin.eatnow.dtos.PaymentDto;

public interface PaymentService {
    
    public PaymentDto pay(String orderId, double amount, String details);
    public PaymentDto get(String transactionId);
    public boolean revert(String transactionId);
}
