package com.eatnow.cart.dtos;

import java.util.List;
import java.util.ArrayList;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Builder
@Data
@AllArgsConstructor
public class Cart {

    @Data
    @AllArgsConstructor
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
