package com.eatnow.order.dtos;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

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
    @NotBlank
    private String transactionId;

    @NonNull
    @NotBlank
    private String orderId;

    @NotBlank
    private double total;

    @NonNull
    @Default
    private String paymentDetails = "";

    @NonNull
    @NotBlank
    private String status;

    public static enum PaymentStatus {
        SUCCESS,
        FAILED,
        REVERTED
    }

    @NonNull
    @NotBlank
    private LocalDateTime timeStamp;
}
