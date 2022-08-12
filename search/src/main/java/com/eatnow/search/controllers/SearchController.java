package com.eatnow.search.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.search.dtos.Item;
import com.eatnow.search.dtos.Restaurant;
import com.eatnow.search.exchanges.ItemSearchResponse;
import com.eatnow.search.exchanges.RestaurantSearchResponse;
import com.eatnow.search.services.SearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Search", description = "The search API")
public class SearchController {

    public static final String SEARCH_RESTAURANTS_NEARBY_ENDPOINT = "/search/nearby/restaurants/";
    public static final String SEARCH_RESTAURANTS_NEAR_ADDRESS_ENDPOINT = "/search/near-address/restaurants/";
    public static final String SEARCH_ITEMS_NEARBY_ENDPOINT = "/search/nearby/items/";
    public static final String SEARCH_ITEMS_NEAR_USER_ENDPOINT = "/search/near-address/items/";

    @Autowired
    SearchService searchService;

    private RestaurantSearchResponse getRestaurantSearchResponse(
            Page<Restaurant> restaurantPage) {

        return RestaurantSearchResponse.builder()
                .matches(restaurantPage.getTotalElements())
                .pages(restaurantPage.getTotalPages())
                .pageSize(restaurantPage.getSize())
                .restaurants(restaurantPage.getContent())
                .build();
    }

    private ItemSearchResponse getItemSearchResponse(
            Page<Item> itemPage) {

        return ItemSearchResponse.builder()
                .matches(itemPage.getTotalElements())
                .pages(itemPage.getTotalPages())
                .pageSize(itemPage.getSize())
                .items(itemPage.toList())
                .build();
    }

    @GetMapping(value = SEARCH_RESTAURANTS_NEARBY_ENDPOINT, params = {
            "latitude", "longitude" })
    @Operation(summary = "Search for restaurants near a location specified by latitude and longitude")
    public ResponseEntity<RestaurantSearchResponse> searchRestaurants(
            @Parameter(description = "The search string. If blank, returns all nearby" +
                    "restaurants") @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "page-number", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "20") Integer size) {

        Page<Restaurant> restaurantPage = searchService.searchRestaurantsNearby(
                query, latitude, longitude, page, size);

        RestaurantSearchResponse response = getRestaurantSearchResponse(restaurantPage);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @Operation(summary = "Search for restaurants near the specified user address")
    @GetMapping(value = SEARCH_RESTAURANTS_NEAR_ADDRESS_ENDPOINT, params = {
            "user-id", "address-index" })
    @SecurityRequirement(name = "user token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "address not found")})
    public ResponseEntity<RestaurantSearchResponse> searchRestaurants(
            @Parameter(description = "The search string. If blank, returns all nearby" +
                    "restaurants") @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam("user-id") String userId,
            @Parameter(description = "Index identifying the address." + 
                    " Refer USER api for more information") @RequestParam("address-index") Integer addressIndex,
            @RequestParam(value = "page-number", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "20") Integer size) {

        Page<Restaurant> restaurantPage = searchService.searchRestaurantsNearUserAddress(
                query, userId, addressIndex, page, size);

        RestaurantSearchResponse response = getRestaurantSearchResponse(restaurantPage);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Search for items near a location specified by latitude and longitude")
    @GetMapping(value = SEARCH_ITEMS_NEARBY_ENDPOINT, params = {
            "query", "latitude", "longitude" })
    public ResponseEntity<ItemSearchResponse> searchItems(
            @Parameter(description = "The search string") @RequestParam("query") String query,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "page-number", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "20") Integer size) {

        Page<Item> itemPage = searchService.searchItemsNearby(
                query, latitude, longitude, page, size);

        ItemSearchResponse response = getItemSearchResponse(itemPage);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @Operation(summary = "Search for items near the specified user address")
    @GetMapping(value = SEARCH_ITEMS_NEAR_USER_ENDPOINT, params = {
            "query", "user-id", "address-index" })
    @SecurityRequirement(name = "user token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "address not found")})
    public ResponseEntity<ItemSearchResponse> searchItems(
            @Parameter(description = "The search string") @RequestParam("query") String query,
            @RequestParam("user-id") String userId,
            @Parameter(description = "Index identifying the address." + 
                    " Refer USER api for more information") @RequestParam("address-index") Integer addressIndex,
            @RequestParam(value = "page-number", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "20") Integer size) {

        Page<Item> itemPage = searchService.searchItemsNearUserAddress(
                query, userId, addressIndex, page, size);

        ItemSearchResponse response = getItemSearchResponse(itemPage);
        return ResponseEntity.ok().body(response);
    }
}
