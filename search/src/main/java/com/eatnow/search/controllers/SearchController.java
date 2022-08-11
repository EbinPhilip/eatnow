package com.eatnow.search.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.search.dtos.ItemDto;
import com.eatnow.search.dtos.RestaurantDto;
import com.eatnow.search.exchanges.ItemSearchResponse;
import com.eatnow.search.exchanges.RestaurantSearchResponse;
import com.eatnow.search.services.SearchService;

@RestController
public class SearchController {

    public static final String SEARCH_RESTAURANTS_ENDPOINT = "/search/restaurants/";
    public static final String SEARCH_ITEMS_ENDPOINT = "/search/items/";

    @Autowired
    SearchService searchService;

    @GetMapping(SEARCH_RESTAURANTS_ENDPOINT)
    public ResponseEntity<RestaurantSearchResponse> searchRestaurants(
            @RequestParam("query") String query,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {

        Page<RestaurantDto> restaurantPage = searchService.searchRestaurants(
                    query, latitude, longitude, 0, 20);

        RestaurantSearchResponse response = RestaurantSearchResponse.builder()
                .matches(restaurantPage.getTotalElements())
                .pages(restaurantPage.getTotalPages())
                .pageSize(restaurantPage.getSize())
                .restaurants(restaurantPage.getContent())
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(SEARCH_ITEMS_ENDPOINT)
    public ResponseEntity<ItemSearchResponse> searchItems(
            @RequestParam("query") String query,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {

        Page<ItemDto> itemPage = searchService.searchItems(
                query, latitude, longitude, 0, 20);

        ItemSearchResponse response = ItemSearchResponse.builder()
                .matches(itemPage.getTotalElements())
                .pages(itemPage.getTotalPages())
                .pageSize(itemPage.getSize())
                .items(itemPage.toList())
                .build();
        return ResponseEntity.ok().body(response);
    }
}
