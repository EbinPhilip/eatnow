package com.eatnow.cart.repositories;

import com.eatnow.cart.entities.CartEntity;

public interface CartRepository {
    public CartEntity findById(String userId);

    public boolean existsById(String userId);

    public CartEntity save(CartEntity cart);

    public boolean delete(String userId);
}
