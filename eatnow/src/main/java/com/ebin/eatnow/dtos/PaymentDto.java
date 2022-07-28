package com.ebin.eatnow.dtos;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    @NonNull
    @NotBlank
    private String transactionId;

    @NonNull
    @NotBlank
    private String orderId;

    @NonNull
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
