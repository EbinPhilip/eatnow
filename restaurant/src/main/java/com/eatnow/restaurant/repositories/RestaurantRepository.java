package com.eatnow.restaurant.repositories;

import java.util.List;
import java.util.Map;

import com.eatnow.restaurant.entities.RestaurantEntity;
import com.eatnow.restaurant.utils.Location;

public interface RestaurantRepository {
    public RestaurantEntity findById(String id);

    public List<RestaurantEntity> findById(List<String> id);

    public Map<String, RestaurantEntity> findOpenRestaurantsNearby(Location location, double distance);

    public boolean existsById(String id);

    public RestaurantEntity update(RestaurantEntity restaurant);
}
