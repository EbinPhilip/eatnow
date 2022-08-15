package com.eatnow.order.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    private int itemIndex;

    @NonNull
    private String itemName;

    private double price;

    private int quantity;
}
