package com.eatnow.restaurant.repositories.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Repository;

import com.eatnow.restaurant.entities.Restaurant;
import com.eatnow.restaurant.repositories.RestaurantRepository;
import com.eatnow.restaurant.repositories.mongoDao.RestaurantMongoDao;
import com.eatnow.restaurant.utils.Location;
import com.eatnow.restaurant.utils.RedisCache;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;

@Primary
@Repository
public class RestaurantRepositoryMongo implements RestaurantRepository {

    private static final Duration restaurantsNearbyCacheTimeout = Duration.ofMinutes(5);

    private static final String distancePrefix = ":";
    private static final String restaurantPrefix = "r";

    @Autowired
    private RestaurantMongoDao mongoDao;

    @Autowired
    private RedisCache cache;

    public Restaurant findById(String id) {

        return findByIdFromCache(id);
    }

    private Restaurant findByIdFromDb(String id) {

        return mongoDao.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    private Restaurant findByIdFromCache(String id) {

        Restaurant restaurant = null;

        try (Jedis jedis = cache.getResource()) {

            String restaurantJson = jedis.hget(id, restaurantPrefix);
            if (restaurantJson == null) {

                restaurant = findByIdFromDb(id);
                restaurantJson = toJson(restaurant);
                jedis.hset(id, restaurantPrefix, restaurantJson);
            } else {

                restaurant = fromJson(restaurantJson);
            }
        }

        return restaurant;
    }

    public List<Restaurant> findById(List<String> ids) {

        List<Restaurant> restaurants = new ArrayList<>();
        mongoDao.findAllById(ids).forEach(restaurants::add);
        return restaurants;
    }

    private Restaurant fromJson(String restaurantJson) {

        try {
            return new ObjectMapper()
                    .readValue(restaurantJson, Restaurant.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String toJson(Restaurant restaurant) {

        try {
            return new ObjectMapper()
                    .writeValueAsString(restaurant);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public Map<String, Restaurant> findOpenRestaurantsNearby(Location location, double distance) {

        return findNearbyRestaurantsFromCache(location, distance)
                .stream()
                .collect(Collectors.toMap((i) -> (i.getId()), (i) -> i));
    }

    private List<Restaurant> findNearbyRestaurantsFromDb(Location location, double distance) {

        return mongoDao.findByLocationNear(location.getPoint(),
                new Distance(distance, Metrics.KILOMETERS));
    }

    private List<Restaurant> findNearbyRestaurantsFromCache(Location location, double distance) {

        String key = location.getGeoHash().toBase32() + distancePrefix
                + String.valueOf((int) distance);
        List<Restaurant> restaurants = new ArrayList<>();

        try (Jedis jedis = cache.getResource()) {

            String restaurantsListJson = jedis.get(key);
            if (restaurantsListJson == null) {

                restaurants = findNearbyRestaurantsFromDb(location, distance);
                try {
                    restaurantsListJson = new ObjectMapper()
                            .writeValueAsString(restaurants);
                    jedis.setex(key, restaurantsNearbyCacheTimeout.getSeconds(),
                            restaurantsListJson);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            } else {

                try {
                    restaurants = new ObjectMapper().readValue(restaurantsListJson,
                            new TypeReference<List<Restaurant>>() {
                            });
                    jedis.expire(key, restaurantsNearbyCacheTimeout.getSeconds());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
        }

        return restaurants;
    }

    public boolean existsById(String id) {

        try (Jedis jedis = cache.getResource()) {

            if (jedis.exists(id)) {
                return true;
            }
        }
        return mongoDao.existsById(id);
    }

    public Restaurant update(Restaurant restaurant) {

        try (Jedis jedis = cache.getResource()) {

            jedis.hset(restaurant.getId(), restaurantPrefix, toJson(restaurant));
        }
        return mongoDao.save(restaurant);
    }
}
