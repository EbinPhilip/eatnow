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
import lombok.Builder.Default;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        @NonNull
        private Integer itemIndex;
        
        private Double price;
        
        @NonNull
        private Integer quantity;
    }

    private String orderId;

    @NonNull
    private String userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Default
    private Integer addressIndex = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Default
    private String address = null;

    @NonNull
    private String restaurantId;

    @NonNull
    @Default
    List<Order.Item> items = new ArrayList<>();

    private Double total;

    private String transactionId;

    private LocalDateTime timeStamp;

    private String status;
}
