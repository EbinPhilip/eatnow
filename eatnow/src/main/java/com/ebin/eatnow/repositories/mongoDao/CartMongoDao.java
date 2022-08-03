package com.ebin.eatnow.repositories.mongoDao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ebin.eatnow.entities.Cart;

public interface CartMongoDao extends MongoRepository<Cart, String> {

}
