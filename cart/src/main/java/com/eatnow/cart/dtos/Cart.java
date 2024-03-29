package com.eatnow.cart.dtos;

import java.util.List;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Builder.Default;

@Builder
@Data
@AllArgsConstructor
public class Cart {

    @Data
    @AllArgsConstructor
    public static class CartItem {

        @NonNull
        private String name;

        private int itemIndex;
    
        private double price;
    
        private int quantity;
    }

    @NonNull
    private String userId;

    @NonNull
    private String restaurantId;

    @NonNull
    private String restaurantName;

    @NonNull
    @Default
    List<Cart.CartItem> items = new ArrayList<>();

    @NonNull
    private Double total;
}
