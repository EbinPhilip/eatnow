package com.eatnow.restaurant.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eatnow.restaurant.dtos.Restaurant;
import com.eatnow.restaurant.entities.ItemElasticEntity;
import com.eatnow.restaurant.entities.RestaurantElasticEntity;
import com.eatnow.restaurant.entities.RestaurantEntity;
import com.eatnow.restaurant.repositories.RestaurantRepository;
import com.eatnow.restaurant.repositories.elasticsearch.ItemElasticRepository;
import com.eatnow.restaurant.repositories.elasticsearch.RestaurantElasticRepository;
import com.eatnow.restaurant.utils.Location;

@Service
public class RestaurantService {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantElasticRepository restaurantElasticRepository;
    @Autowired
    ItemElasticRepository itemElasticRepository;

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

        RestaurantElasticEntity elasticRestaurant = restaurantElasticRepository.findById(restaurantId)
                .orElseThrow(() -> (new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "restaurant not found")));
        elasticRestaurant.setOpen(openStatus);
        restaurantElasticRepository.save(elasticRestaurant);

        return restaurant.isOpen();
    }

    public Restaurant updateRestaurant(Restaurant dto) {

        RestaurantEntity old = restaurantRepository.findById(dto.getId());
        RestaurantEntity restaurant = dtoToRestaurant(dto, old);

        restaurant = restaurantRepository.update(restaurant);

        // updates elasticsearch restaurant index
        RestaurantElasticEntity elasticRestaurant = restaurantElasticRepository.findById(restaurant.getId())
                .orElseThrow(() -> (new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "restaurant not found")));
        elasticRestaurant.setName(restaurant.getName());
        elasticRestaurant.setTags(String.join(" ", restaurant.getTags()));
        restaurantElasticRepository.save(elasticRestaurant);

        // update elasticsearch item index
        List<ItemElasticEntity> itemsElastic = itemElasticRepository
                .findByRestaurantId(restaurant.getId());
        for (ItemElasticEntity itemE : itemsElastic) {
            itemE.setRestaurantName(restaurant.getName());
            itemElasticRepository.save(itemE);
        }

        return restaurantToDto(restaurant);
    }

    public boolean isRestaurantServiceable(String restaurantId, Location location) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId);

        if (restaurant.isOpen() && location.getDistanceFrom(
                new Location(restaurant.getLocation())) <= max_distance) {
            return true;
        } else {
            return false;
        }
    }

    private RestaurantEntity dtoToRestaurant(Restaurant dto, RestaurantEntity old) {

        RestaurantEntity restaurant = RestaurantEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(old.getAddress())
                .location(old.getLocation())
                .tags(dto.getTags())
                .rating(old.getRating())
                .reviews(old.getReviews())
                .isOpen(old.isOpen())
                .build();
        return restaurant;
    }

    Location getRestaurantLocation(String restaurantId) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId);
        Location location = new Location(restaurant.getLocation().getX(),
                restaurant.getLocation().getY());
        return location;
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
