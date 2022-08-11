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

    private RestaurantSearchResponse getRestaurantSearchResponse(
            Page<RestaurantDto> restaurantPage) {

        return RestaurantSearchResponse.builder()
                .matches(restaurantPage.getTotalElements())
                .pages(restaurantPage.getTotalPages())
                .pageSize(restaurantPage.getSize())
                .restaurants(restaurantPage.getContent())
                .build();
    }

    private ItemSearchResponse getItemSearchResponse(
            Page<ItemDto> itemPage) {

        return ItemSearchResponse.builder()
                .matches(itemPage.getTotalElements())
                .pages(itemPage.getTotalPages())
                .pageSize(itemPage.getSize())
                .items(itemPage.toList())
                .build();
    }

    @GetMapping(value = SEARCH_RESTAURANTS_ENDPOINT, params = {
            "query", "latitude", "longitude" })
    public ResponseEntity<RestaurantSearchResponse> searchRestaurants(
            @RequestParam("query") String query,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "page-number", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "20") Integer size) {

        Page<RestaurantDto> restaurantPage = searchService.searchRestaurantsNearby(
                query, latitude, longitude, page, size);

        RestaurantSearchResponse response = getRestaurantSearchResponse(restaurantPage);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = SEARCH_RESTAURANTS_ENDPOINT, params = {
            "query", "user-id", "address-index" })
    public ResponseEntity<RestaurantSearchResponse> searchRestaurants(
            @RequestParam("query") String query,
            @RequestParam("user-id") String userId,
            @RequestParam("address-index") Integer addressIndex,
            @RequestParam(value = "page-number", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "20") Integer size) {

        Page<RestaurantDto> restaurantPage = searchService.searchRestaurantsNearUserAddress(
                query, userId, addressIndex, page, size);

        RestaurantSearchResponse response = getRestaurantSearchResponse(restaurantPage);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = SEARCH_ITEMS_ENDPOINT, params = {
            "query", "latitude", "longitude" })
    public ResponseEntity<ItemSearchResponse> searchItems(
            @RequestParam("query") String query,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "page-number", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "20") Integer size) {

        Page<ItemDto> itemPage = searchService.searchItemsNearby(
                query, latitude, longitude, page, size);

        ItemSearchResponse response = getItemSearchResponse(itemPage);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = SEARCH_ITEMS_ENDPOINT, params = {
            "query", "user-id", "address-index" })
    public ResponseEntity<ItemSearchResponse> searchItems(
            @RequestParam("query") String query,
            @RequestParam("user-id") String userId,
            @RequestParam("address-index") Integer addressIndex,
            @RequestParam(value = "page-number", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "20") Integer size) {

        Page<ItemDto> itemPage = searchService.searchItemsNearUserAddress(
                query, userId, addressIndex, page, size);

        ItemSearchResponse response = getItemSearchResponse(itemPage);
        return ResponseEntity.ok().body(response);
    }
}
