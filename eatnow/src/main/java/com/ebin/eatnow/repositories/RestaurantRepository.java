package com.ebin.eatnow.repositories;

import java.util.List;
import java.util.Map;

import com.ebin.eatnow.entities.Restaurant;

public interface RestaurantRepository {
    Restaurant findById(String id);

    List<Restaurant> findById(List<String> ids);

    Map<String, Restaurant> findOpenRestaurantsNearby
        (double latitude, double longitude, double distance);

    boolean exists(String id);

    Restaurant save(Restaurant restaurant);
}
