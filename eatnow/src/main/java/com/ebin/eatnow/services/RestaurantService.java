package com.ebin.eatnow.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebin.eatnow.dtos.RestaurantDto;
import com.ebin.eatnow.entities.Restaurant;
import com.ebin.eatnow.repositories.RestaurantRepository;
import com.ebin.eatnow.utils.Location;

@Service
public class RestaurantService {
    @Autowired
    RestaurantRepository restaurantRepository;

    private static double max_distance = 20.0;

    public RestaurantDto getRestaurantbyId(String id)
    {
        Restaurant restaurant = 
            Optional.ofNullable(restaurantRepository.findById(id))
            .orElseThrow(RuntimeException::new);
        return restaurantToDto(restaurant);
    }

    public List<RestaurantDto> getRestaurantsNearby(double latitude, double longitude)
    {
        Location location = new Location(latitude, longitude);
        Map<String,Restaurant> restaurants = 
            restaurantRepository.findOpenRestaurantsNearby(location, max_distance);
        
        List<RestaurantDto> restaurantList =
            restaurants.values().stream().map(
                (i) -> {
                    return restaurantToDto(i);
                }
            ).collect(Collectors.toList());
        return restaurantList;
    }

    public boolean isRestaurantOpen(String restaurantId) {

        Restaurant restaurant = 
            Optional.ofNullable(restaurantRepository.findById(restaurantId))
            .orElseThrow(RuntimeException::new);

        return restaurant.isOpen();
    }

    public boolean setRestaurantOpen(String restaurantId, boolean openStatus) {

        Restaurant restaurant = 
            Optional.ofNullable(restaurantRepository.findById(restaurantId))
            .orElseThrow(RuntimeException::new);

        restaurant.setOpen(openStatus);
        restaurantRepository.update(restaurant);

        return restaurant.isOpen();
    }

    public RestaurantDto updateRestaurant(RestaurantDto dto)
    {
        Restaurant old = restaurantRepository.findById(dto.getId());
        Restaurant restaurant = dtoToRestaurant(dto, old);
        
        restaurant = restaurantRepository.update(restaurant);
        return restaurantToDto(restaurant);
    }

    public boolean isRestaurantServiceable(String restaurantId, Location location) {

        return true;
    }

    private Restaurant dtoToRestaurant(RestaurantDto dto, Restaurant old)
    {
        Restaurant restaurant = Restaurant.builder()
            .id(dto.getId())
            .name(dto.getName())
            .address(dto.getAddress())
            .location(old.getLocation())
            .tags(dto.getTags())
            .rating(old.getRating())
            .reviews(old.getReviews())
            .isOpen(old.isOpen())
            .build();
        return restaurant;
    }

    private RestaurantDto restaurantToDto(Restaurant restaurant)
    {
        RestaurantDto dto = RestaurantDto.builder()
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
