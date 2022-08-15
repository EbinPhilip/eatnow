package com.eatnow.order.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Builder.Default;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @NoArgsConstructor
    public static class Item {

        private String name;

        @NonNull
        private Integer itemIndex;
        
        private Double price;
        
        @NonNull
        private Integer quantity;
    }

    private String orderId;

    @NonNull
    private String userId;

    private String userName;

    private String phone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Default
    private Integer addressIndex = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Default
    private String address = null;

    @NonNull
    private String restaurantId;

    private String restaurantName;

    @NonNull
    @Default
    List<Order.Item> items = new ArrayList<>();

    private Double total;

    private String transactionId;

    private LocalDateTime timeStamp;

    private String status;
}
