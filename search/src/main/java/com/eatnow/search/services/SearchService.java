package com.eatnow.search.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eatnow.search.dtos.ItemDto;
import com.eatnow.search.dtos.RestaurantDto;
import com.eatnow.search.entities.Item;
import com.eatnow.search.entities.Restaurant;
import com.eatnow.search.repositories.ItemRepository;
import com.eatnow.search.repositories.RestaurantRepository;

@Service
public class SearchService {

    private static double maxDistanceInKm = 10;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ItemRepository itemRepository;

    public Page<RestaurantDto> searchRestaurants(String query,
            double latitude, double longitude, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurantPage = restaurantRepository.findBySearchQueryNear(
                query, maxDistanceInKm, latitude, longitude, pageable);

        return restaurantPage.map(
                (i) -> {
                    return RestaurantDto.builder()
                            .id(i.getId())
                            .name(i.getName())
                            .address(i.getAddress())
                            .tags(Arrays.asList(i.getTags().split(" ")))
                            .rating(i.getRating())
                            .build();
                });
    }

    public Page<ItemDto> searchItems(String query,
            double latitude, double longitude, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Item> itemPage = itemRepository.findBySearchQueryNear(
                query, maxDistanceInKm, latitude, longitude, pageable);

        return itemPage.map(
                (i) -> {
                    return ItemDto.builder()
                            .name(i.getName())
                            .itemIndex(i.getItemIndex())
                            .restaurantId(i.getRestaurantId())
                            .restaurantName(i.getRestaurantName())
                            .price(i.getPrice())
                            .description(i.getDescription())
                            .tags(Arrays.asList(i.getTags().split(" ")))
                            .build();
                });
    }
}
