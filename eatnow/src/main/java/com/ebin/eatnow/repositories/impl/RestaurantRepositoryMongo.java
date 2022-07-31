package com.ebin.eatnow.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Repository;

import com.ebin.eatnow.entities.Restaurant;
import com.ebin.eatnow.repositories.RestaurantRepository;
import com.ebin.eatnow.repositories.dao.RestaurantMongoDao;
import com.ebin.eatnow.utils.Location;

@Primary
@Repository
public class RestaurantRepositoryMongo implements RestaurantRepository {

    @Autowired
    private RestaurantMongoDao mongoDao;

    public Restaurant findById(String id) {

        return mongoDao.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public List<Restaurant> findById(List<String> ids) {

        List<Restaurant> restaurants = new ArrayList<>();
        mongoDao.findAllById(ids).forEach(restaurants::add);
        return restaurants;
    }

    public Map<String, Restaurant> findOpenRestaurantsNearby(Location location, double distance) {

        return mongoDao.findByLocationNear(location.getPoint(),
                new Distance(distance, Metrics.KILOMETERS))
                .stream()
                .collect(Collectors.toMap((i) -> (i.getId()), (i) -> i));
    }

    public boolean existsById(String id) {

        return mongoDao.existsById(id);
    }

    public Restaurant update(Restaurant restaurant) {

        return mongoDao.save(restaurant);
    }

    public boolean isRestaurantServiceable(String restaurantId,
            Location location, double distance) {

        return true;
    }
}
