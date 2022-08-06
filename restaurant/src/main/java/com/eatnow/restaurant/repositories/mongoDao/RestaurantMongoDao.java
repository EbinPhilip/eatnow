package com.eatnow.restaurant.repositories.mongoDao;

import java.util.List;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.eatnow.restaurant.entities.Restaurant;


public interface RestaurantMongoDao extends MongoRepository<Restaurant, String> {
    
    List<Restaurant> findByLocationNear(Point location, Distance distance);
}
