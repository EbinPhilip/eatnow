package com.eatnow.cart.repositories;

import com.eatnow.cart.entities.Cart;

public interface CartRepository {
    public Cart findById(String userId);

    public boolean existsById(String userId);

    public Cart save(Cart cart);

    public boolean delete(String userId);
}
