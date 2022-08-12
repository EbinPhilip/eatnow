package com.eatnow.cart.repositories.dummy;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.eatnow.cart.entities.CartEntity;
import com.eatnow.cart.repositories.CartRepository;

@Repository
public class CartRepositoryDummy implements CartRepository {
    private HashMap<String, CartEntity> carts = new HashMap<>();

    public CartEntity findById(String userId) {
        return carts.get(userId);
    }

    public boolean existsById(String userId) {
        return carts.containsKey(userId);
    }

    public CartEntity save(CartEntity cart) {
        carts.put(cart.getUserId(), cart);
        return cart;
    }

    public boolean delete(String userId) {
        carts.remove(userId);
        return true;
    }
}
