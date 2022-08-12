package com.eatnow.cart.repositories.mongoDao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eatnow.cart.entities.CartEntity;

public interface CartMongoDao extends MongoRepository<CartEntity, String> {

}
