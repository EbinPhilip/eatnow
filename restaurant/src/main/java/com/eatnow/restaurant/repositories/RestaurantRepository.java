package com.eatnow.restaurant.repositories;

import java.util.List;
import java.util.Map;

import com.eatnow.restaurant.entities.Restaurant;
import com.eatnow.restaurant.utils.Location;

public interface RestaurantRepository {
    public Restaurant findById(String id);

    public List<Restaurant> findById(List<String> id);

    public Map<String, Restaurant> findOpenRestaurantsNearby(Location location, double distance);

    public boolean existsById(String id);

    public Restaurant update(Restaurant restaurant);
}
