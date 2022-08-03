package com.ebin.eatnow.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.ebin.eatnow.entities.Cart;
import com.ebin.eatnow.repositories.CartRepository;
import com.ebin.eatnow.repositories.mongoDao.CartMongoDao;

@Repository
@Primary
public class CartRepositoryMongo implements CartRepository {

    @Autowired
    private CartMongoDao dao;

    public Cart findById(String userId) {

        return dao.findById(userId)
                .orElse(null);
    }

    public boolean existsById(String userId) {

        return dao.existsById(userId);
    }

    public Cart save(Cart cart) {
    
        return dao.save(cart);
    }

    public boolean delete(String userId) {
    
        dao.deleteById(userId);
        return true;
    }
}
