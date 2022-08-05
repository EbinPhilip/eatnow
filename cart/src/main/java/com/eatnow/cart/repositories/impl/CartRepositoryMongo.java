package com.eatnow.cart.repositories.impl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.eatnow.cart.entities.Cart;
import com.eatnow.cart.repositories.CartRepository;
import com.eatnow.cart.repositories.mongoDao.CartMongoDao;
import com.eatnow.cart.utils.RedisCache;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;

@Repository
@Primary
public class CartRepositoryMongo implements CartRepository {

    private static final Duration cartCacheTimeout = Duration
            .ofSeconds(600);

    @Autowired
    private CartMongoDao dao;

    @Autowired
    private RedisCache cache;

    public Cart findById(String userId) {

        return findByIdCached(userId);
    }

    private Cart findByIdCached(String userId) {

        Cart cart = null;

        try(Jedis jedis = cache.getResource()) {

            String cartJson = jedis.get(userId);
            if (cartJson == null) {

                cart = dao.findById(userId)
                        .orElse(null);
                if (cart != null) {
                    jedis.setex(userId,
                            cartCacheTimeout.getSeconds(), toJson(cart));
                }
            } else {

                cart = fromJson(cartJson);
            }
        }

        return cart;
    }

    private Cart fromJson(String cartJson) {

        try {
            return new ObjectMapper()
                    .readValue(cartJson, Cart.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String toJson(Cart cart) {

        try {
            return new ObjectMapper()
                    .writeValueAsString(cart);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public boolean existsById(String userId) {

        try (Jedis jedis = cache.getResource()) {

            if (jedis.get(userId) != null) {
                return true;
            }
        }
        return dao.existsById(userId);
    }

    public Cart save(Cart cart) {

        try (Jedis jedis = cache.getResource()) {
            jedis.setex(cart.getUserId(), cartCacheTimeout.getSeconds(), toJson(cart));
        }

        return dao.save(cart);
    }

    public boolean delete(String userId) {

        try (Jedis jedis = cache.getResource()) {
            jedis.del(userId);
        }

        dao.deleteById(userId);
        return true;
    }
}
