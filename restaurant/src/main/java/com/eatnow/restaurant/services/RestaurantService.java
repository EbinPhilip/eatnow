package com.eatnow.restaurant.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatnow.restaurant.dtos.Restaurant;
import com.eatnow.restaurant.entities.RestaurantEntity;
import com.eatnow.restaurant.repositories.RestaurantRepository;
import com.eatnow.restaurant.utils.Location;

@Service
public class RestaurantService {
    @Autowired
    RestaurantRepository restaurantRepository;

    private static double max_distance = 20.0;

    public Restaurant getRestaurantbyId(String id) {

        RestaurantEntity restaurant = restaurantRepository.findById(id);

        return restaurantToDto(restaurant);
    }

    public List<Restaurant> getRestaurantsNearby(double latitude, double longitude) {

        Location location = new Location(latitude, longitude);
        Map<String, RestaurantEntity> restaurants = restaurantRepository.findOpenRestaurantsNearby(location,
                max_distance);

        List<Restaurant> restaurantList = restaurants.values().stream().map(
                (i) -> {
                    return restaurantToDto(i);
                }).collect(Collectors.toList());
        return restaurantList;
    }

    public boolean isRestaurantOpen(String restaurantId) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId);

        return restaurant.isOpen();
    }

    public boolean setRestaurantOpen(String restaurantId, boolean openStatus) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId);

        restaurant.setOpen(openStatus);
        restaurantRepository.update(restaurant);

        return restaurant.isOpen();
    }

    public Restaurant updateRestaurant(Restaurant dto) {

        RestaurantEntity old = restaurantRepository.findById(dto.getId());
        RestaurantEntity restaurant = dtoToRestaurant(dto, old);

        restaurant = restaurantRepository.update(restaurant);
        return restaurantToDto(restaurant);
    }

    public boolean isRestaurantServiceable(String restaurantId, Location location) {

        return true;
    }

    private RestaurantEntity dtoToRestaurant(Restaurant dto, RestaurantEntity old) {

        RestaurantEntity restaurant = RestaurantEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .location(old.getLocation())
                .tags(dto.getTags())
                .rating(old.getRating())
                .reviews(old.getReviews())
                .isOpen(dto.isOpen())
                .build();
        return restaurant;
    }

    private Restaurant restaurantToDto(RestaurantEntity restaurant) {

        Restaurant dto = Restaurant.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .tags(restaurant.getTags())
                .rating(restaurant.getRating())
                .isOpen(restaurant.isOpen())
                .build();
        return dto;
    }
}
