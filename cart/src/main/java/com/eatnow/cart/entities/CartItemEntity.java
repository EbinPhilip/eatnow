package com.eatnow.cart.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CartItemEntity {

    @NonNull
    private String name;
    
    private int itemIndex;

    private double price;

    private int quantity;
}
