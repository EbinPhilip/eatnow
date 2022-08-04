package com.ebin.eatnow.repositories.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Repository;

import com.ebin.eatnow.entities.Restaurant;
import com.ebin.eatnow.repositories.RestaurantRepository;
import com.ebin.eatnow.repositories.mongoDao.RestaurantMongoDao;
import com.ebin.eatnow.utils.Location;
import com.ebin.eatnow.utils.RedisCache;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;

@Primary
@Repository
public class RestaurantRepositoryMongo implements RestaurantRepository {

    private static final Duration restaurantEntryCacheTimeout = Duration.ofSeconds(900);
    private static final Duration restaurantsNearbyCacheTimeout = Duration.ofSeconds(300);

    private static final String distancePrefix = ":";

    @Autowired
    private RestaurantMongoDao mongoDao;

    private RedisCache cache;

    @Value("${restaurant.redis.host}")
    private String redisHost;

    @Value("${restaurant.redis.port}")
    private int redisPort;

    @PostConstruct
    private void initCache() {

        cache = new RedisCache(redisHost, redisPort);
    }

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

            String restaurantJson = jedis.get(id);
            if (restaurantJson == null) {

                restaurant = findByIdFromDb(id);
                restaurantJson = toJson(restaurant);
                jedis.setex(id, restaurantEntryCacheTimeout.getSeconds(), restaurantJson);
            } else {

                restaurant = fromJson(restaurantJson);
                jedis.expire(id, restaurantEntryCacheTimeout.getSeconds());
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

            if (jedis.get(id) != null) {
                return true;
            }
        }
        return mongoDao.existsById(id);
    }

    public Restaurant update(Restaurant restaurant) {

        try (Jedis jedis = cache.getResource()) {

            jedis.setex(restaurant.getId(), restaurantEntryCacheTimeout.getSeconds(),
                    toJson(restaurant));
        }
        return mongoDao.save(restaurant);
    }
}
