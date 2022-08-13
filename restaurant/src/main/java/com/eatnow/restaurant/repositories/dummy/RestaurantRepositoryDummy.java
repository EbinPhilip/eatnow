package com.eatnow.restaurant.repositories.dummy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Repository;

import com.eatnow.restaurant.entities.RestaurantEntity;
import com.eatnow.restaurant.repositories.RestaurantRepository;
import com.eatnow.restaurant.utils.Location;

@Repository
public class RestaurantRepositoryDummy implements RestaurantRepository {
    private HashMap<String, RestaurantEntity> restaurants;

    public RestaurantRepositoryDummy() {
        restaurants = new HashMap<>();

        RestaurantEntity r1 = RestaurantEntity.builder()
                .id("r1")
                .name("r1")
                .address("")
                .location(new GeoJsonPoint(-73.9667, 40.78))
                .tags(Arrays.asList("food", "yummy"))
                .rating(4.4)
                .reviews(10)
                .build();
        RestaurantEntity r2 = RestaurantEntity.builder()
                .id("r2")
                .name("r2")
                .address("")
                .location(new GeoJsonPoint(-73.9667, 40.78))
                .rating(2.4)
                .reviews(50)
                .build();
        RestaurantEntity r3 = RestaurantEntity.builder()
                .id("r3")
                .name("r3")
                .address("")
                .location(new GeoJsonPoint(-73.9667, 40.78))
                .tags(Arrays.asList(""))
                .rating(3.4)
                .reviews(10)
                .build();
        RestaurantEntity r4 = RestaurantEntity.builder()
                .id("r4")
                .name("r4")
                .address("")
                .location(new GeoJsonPoint(-73.9667, 40.78))
                .tags(Arrays.asList("yummy"))
                .build();
        restaurants.put(r1.getId(), r1);
        restaurants.put(r2.getId(), r2);
        restaurants.put(r3.getId(), r3);
        restaurants.put(r4.getId(), r4);
    }

    @Override
    public RestaurantEntity findById(String id) {
        return restaurants.get(id);
    }

    @Override
    public List<RestaurantEntity> findById(List<String> id) {
        return restaurants.values().stream()
                .filter((i) -> (id.contains(i.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, RestaurantEntity> findOpenRestaurantsNearby(Location location, double distance) {
        return restaurants.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public boolean existsById(String id) {
        return restaurants.containsKey(id);
    }

    @Override
    public RestaurantEntity update(RestaurantEntity restaurant) {
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant;
    }
}
