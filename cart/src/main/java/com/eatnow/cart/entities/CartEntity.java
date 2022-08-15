package com.eatnow.cart.entities;

import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Document("cart")
public class CartEntity {
    @NonNull
    @Id
    private String userId;

    @NonNull
    private String restaurantId;

    @NonNull
    private String restaurantName;

    LinkedHashMap<Integer, CartItemEntity> items = new LinkedHashMap<>();

    private double total = 0.0;

    public CartEntity add(CartItemEntity item)
    {
        if (item.getQuantity() == 0) {
            return this;
        }

        CartItemEntity existing = items.get(item.getItemIndex());
        
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

    public CartEntity delete(int itemIndex)
    {
        CartItemEntity existing = Optional.ofNullable(
            items.get(itemIndex))
            .orElseThrow(()->(new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "item not present in cart")));
        
        total = total - (existing.getPrice() * existing.getQuantity());
        
        items.remove(itemIndex);
        return this;
    }

    public CartEntity update(int itemIndex, int quantity)
    {
        if (quantity == 0) {
            return delete(itemIndex);
        }

        CartItemEntity existing = Optional.ofNullable(
            items.get(itemIndex))
            .orElseThrow(()->(new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "item not present in cart")));
        
        double difference = (quantity - existing.getQuantity()) * existing.getPrice();
        existing.setQuantity(quantity);
        total = total + difference;

        return this;
    }

}
