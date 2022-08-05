package com.eatnow.cart.repositories.dummy;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.eatnow.cart.entities.Cart;
import com.eatnow.cart.repositories.CartRepository;

@Repository
public class CartRepositoryDummy implements CartRepository {
    private HashMap<String, Cart> carts = new HashMap<>();

    public Cart findById(String userId) {
        return carts.get(userId);
    }

    public boolean existsById(String userId) {
        return carts.containsKey(userId);
    }

    public Cart save(Cart cart) {
        carts.put(cart.getUserId(), cart);
        return cart;
    }

    public boolean delete(String userId) {
        carts.remove(userId);
        return true;
    }
}
