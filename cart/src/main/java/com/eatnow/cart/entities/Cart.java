package com.eatnow.cart.entities;

import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Cart {
    @NonNull
    @Id
    private String userId;

    @NonNull
    private String restaurantId;

    LinkedHashMap<Integer, CartItem> items = new LinkedHashMap<>();

    private double total = 0.0;

    public Cart add(CartItem item)
    {
        CartItem existing = items.get(item.getItemIndex());
        
        if (existing == null)
        {
            items.put(item.getItemIndex(), item);
        }
        else
        {
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
        }
        total = total + item.getPrice()*item.getQuantity();
        return this;
    }

    public Cart delete(int itemIndex)
    {
        CartItem existing = Optional.ofNullable(
            items.get(itemIndex))
            .orElseThrow(RuntimeException::new);
        
        total = total - (existing.getPrice() * existing.getQuantity());
        
        items.remove(itemIndex);
        return this;
    }

    public Cart update(int itemIndex, int quantity)
    {
        CartItem existing = Optional.ofNullable(
            items.get(itemIndex))
            .orElseThrow(RuntimeException::new);
        
        double difference = (quantity - existing.getQuantity()) * existing.getPrice();
        existing.setQuantity(quantity);
        total = total + difference;

        return this;
    }

}
