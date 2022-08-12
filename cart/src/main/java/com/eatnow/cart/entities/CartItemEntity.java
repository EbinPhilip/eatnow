package com.eatnow.cart.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CartItemEntity {
    private int itemIndex;

    private double price;

    private int quantity;
}
