package com.eatnow.order.services;

import com.eatnow.order.dtos.Payment;

public interface PaymentService {
    
    public Payment pay(String orderId, double amount, String details);
    public Payment get(String transactionId);
    public boolean revert(String transactionId);
}
