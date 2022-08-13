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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.eatnow.restaurant.entities.RestaurantEntity;
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

    public RestaurantEntity findById(String id) {

        return findByIdFromCache(id);
    }

    private RestaurantEntity findByIdFromDb(String id) {

        return mongoDao.findById(id)
                .orElseThrow(() -> (new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "restaurant not found")));
    }

    private RestaurantEntity findByIdFromCache(String id) {

        RestaurantEntity restaurant = null;

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

    public List<RestaurantEntity> findById(List<String> ids) {

        List<RestaurantEntity> restaurants = new ArrayList<>();
        mongoDao.findAllById(ids).forEach(restaurants::add);
        return restaurants;
    }

    private RestaurantEntity fromJson(String restaurantJson) {

        try {
            return new ObjectMapper()
                    .readValue(restaurantJson, RestaurantEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String toJson(RestaurantEntity restaurant) {

        try {
            return new ObjectMapper()
                    .writeValueAsString(restaurant);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public Map<String, RestaurantEntity> findOpenRestaurantsNearby(Location location, double distance) {

        return findNearbyRestaurantsFromCache(location, distance)
                .stream()
                .collect(Collectors.toMap((i) -> (i.getId()), (i) -> i));
    }

    private List<RestaurantEntity> findNearbyRestaurantsFromDb(Location location, double distance) {

        return mongoDao.findByLocationNear(location.getPoint(),
                new Distance(distance, Metrics.KILOMETERS));
    }

    private List<RestaurantEntity> findNearbyRestaurantsFromCache(Location location, double distance) {

        String key = location.getGeoHash().toBase32() + distancePrefix
                + String.valueOf((int) distance);
        List<RestaurantEntity> restaurants = new ArrayList<>();

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
                            new TypeReference<List<RestaurantEntity>>() {
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

    public RestaurantEntity update(RestaurantEntity restaurant) {

        try (Jedis jedis = cache.getResource()) {

            jedis.hset(restaurant.getId(), restaurantPrefix, toJson(restaurant));
        }
        return mongoDao.save(restaurant);
    }
}
