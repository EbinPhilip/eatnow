package com.eatnow.restaurant.controllers;

import java.sql.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.restaurant.dtos.Restaurant;
import com.eatnow.restaurant.exchanges.RestaurantEditRequest;
import com.eatnow.restaurant.services.RestaurantService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Restaurant APIs")
public class RestaurantController {
    public static final String RESTAURANTS_ENDPOINT = "/restaurant";
    public static final String RESTAURANTS_LOGIN_ENDPOINT = RESTAURANTS_ENDPOINT + "/login";
    public static final String RESTAURANT_API = RESTAURANTS_ENDPOINT + "/{restaurant-id}";
    public static final String RESTAURANT_OPEN_API = RESTAURANT_API + "/is-open";

    @Autowired
    private RestaurantService restaurantService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @GetMapping(RESTAURANTS_LOGIN_ENDPOINT)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "restaurant not found", content = @Content)})
    @SecurityRequirements()
    @Operation(summary = "Login to get restaurant token", description = "Login with an existing restaurant-id to get restaurant token. No password required.")
    public ResponseEntity<String> login(
            @RequestParam(name = "restaurant-id") String restaurantId) {

        Restaurant restaurant = restaurantService.getRestaurantbyId(restaurantId);
        String key = Jwts
                .builder()
                .setIssuer(issuer)
                .setSubject(restaurant.getId())
                .claim("role", "restaurant")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName()))
                .compact();
        return ResponseEntity.ok().body(key);
    }

    @GetMapping(RESTAURANTS_ENDPOINT)
    @SecurityRequirements()
    @Operation(summary = "Fetch restaurants nearby", description = "Fetch restaurants near the location specified by latitude and longitude")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Request is ill formed or location is invalid", content = @Content)})
    public ResponseEntity<List<Restaurant>> getNearbyRestaurants(
            @RequestParam(name = "latitude") Double latitude,
            @RequestParam(name = "longitude") Double longitude) {

        return ResponseEntity.ok().body(
                restaurantService.getRestaurantsNearby(latitude, longitude));
    }

    @GetMapping(RESTAURANT_API)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "restaurant not found", content = @Content)})
    @SecurityRequirements()
    @Operation(summary = "Fetch restaurant details", description = "Fetch details of restaurant specified by restaurant-id.")
    public ResponseEntity<Restaurant> getRestaurant(
            @PathVariable(name = "restaurant-id") @NotNull String restaurantId) {

        return ResponseEntity.ok().body(
                restaurantService.getRestaurantbyId(restaurantId));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PutMapping(RESTAURANT_OPEN_API)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "restaurant not found", content = @Content)})
    @Operation(summary = "Set restaurant status: open/closed", description = "Set the is-open status of restaurant specified by restaurant-id.")
    public ResponseEntity<Boolean> setRestaurantOpen(
            @PathVariable(name = "restaurant-id") @NotNull String restaurantId,
            @RequestParam(name = "open") boolean openStatus) {

        return ResponseEntity.ok().body(
                restaurantService.setRestaurantOpen(restaurantId, openStatus));
    }

    @GetMapping(RESTAURANT_OPEN_API)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "restaurant not found", content = @Content)})
    @Operation(summary = "Get restaurant status: open/closed", description = "Get the is-open status of restaurant specified by restaurant-id.")
    @SecurityRequirements()
    public ResponseEntity<Boolean> getRestaurantOpen(
            @PathVariable(name = "restaurant-id") @NotNull String restaurantId) {

        return ResponseEntity.ok().body(
                restaurantService.isRestaurantOpen(restaurantId));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PutMapping(RESTAURANT_API)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "restaurant not found", content = @Content)})
    @Operation(summary = "Update restaurant", description = "Update details of the restaurant specified by restaurant-id.")
    public ResponseEntity<Restaurant> putRestaurant(
            @PathVariable(name = "restaurant-id") @NotNull String restaurantId,
            @Valid @RequestBody RestaurantEditRequest restaurantRequest) {

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name(restaurantRequest.getName())
                .tags(restaurantRequest.getTags())
                .build();
        return ResponseEntity.ok().body(
                restaurantService.updateRestaurant(restaurant));
    }

}
