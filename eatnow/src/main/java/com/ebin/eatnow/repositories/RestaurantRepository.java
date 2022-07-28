package com.ebin.eatnow.repositories;

import java.util.List;
import java.util.Map;

import com.ebin.eatnow.entities.Restaurant;
import com.ebin.eatnow.utils.Location;

public interface RestaurantRepository {
    public Restaurant findById(String id);

    public List<Restaurant> findById(List<String> id);

    public Map<String, Restaurant> findOpenRestaurantsNearby(Location location, double distance);

    public boolean existsById(String id);

    public Restaurant update(Restaurant restaurant);

    public boolean isRestaurantServiceable(String restaurantId,
            Location location, double distance);
}
