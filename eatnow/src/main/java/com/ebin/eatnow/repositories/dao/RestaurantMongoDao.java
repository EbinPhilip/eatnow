package com.ebin.eatnow.repositories.dao;

import java.util.List;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ebin.eatnow.entities.Restaurant;


public interface RestaurantMongoDao extends MongoRepository<Restaurant, String> {
    
    List<Restaurant> findByLocationNear(Point location, Distance distance);
}
