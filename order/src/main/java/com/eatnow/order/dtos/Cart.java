package com.eatnow.order.dtos;

import java.util.List;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Builder.Default;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItem {
        private int itemIndex;
    
        private double price;
    
        private int quantity;
    }

    @NonNull
    private String userId;

    @NonNull
    private String restaurantId;

    @NonNull
    @Default
    List<Cart.CartItem> items = new ArrayList<>();

    @NonNull
    private Double total;
}
