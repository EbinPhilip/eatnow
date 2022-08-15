package com.eatnow.order.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Builder.Default;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @NonNull
    private String transactionId;

    @NonNull
    private String orderId;

    private double total;

    @NonNull
    @Default
    private String paymentDetails = "";

    @NonNull
    private String status;

    public static enum PaymentStatus {
        SUCCESS,
        FAILED,
        REVERTED
    }

    @NonNull
    private LocalDateTime timeStamp;
}
