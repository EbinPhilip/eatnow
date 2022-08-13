package com.eatnow.restaurant.controllers;

import java.sql.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eatnow.restaurant.dtos.Restaurant;
import com.eatnow.restaurant.services.RestaurantService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class RestaurantController {
    public static final String RESTAURANTS_ENDPOINT = "/restaurant";
    public static final String RESTAURANTS_LOGIN_ENDPOINT = RESTAURANTS_ENDPOINT + "/login";
    public static final String RESTAURANT_API = RESTAURANTS_ENDPOINT + "/{restaurantId}";
    public static final String RESTAURANT_OPEN_API = RESTAURANT_API + "/is-open";

    @Autowired
    private RestaurantService restaurantService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @GetMapping(RESTAURANTS_LOGIN_ENDPOINT)
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
    public ResponseEntity<List<Restaurant>> getNearbyRestaurants(
            @RequestParam(name = "latitude") Double latitude,
            @RequestParam(name = "longitude") Double longitude) {

        return ResponseEntity.ok().body(
                restaurantService.getRestaurantsNearby(latitude, longitude));
    }

    @GetMapping(RESTAURANT_API)
    public ResponseEntity<Restaurant> getRestaurant(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId) {

        return ResponseEntity.ok().body(
                restaurantService.getRestaurantbyId(restaurantId));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PostMapping(RESTAURANT_OPEN_API)
    public ResponseEntity<Boolean> setRestaurantOpen(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @RequestParam(name = "open") boolean openStatus) {

        return ResponseEntity.ok().body(
                restaurantService.setRestaurantOpen(restaurantId, openStatus));
    }

    @GetMapping(RESTAURANT_OPEN_API)
    public ResponseEntity<Boolean> getRestaurantOpen(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId) {

        return ResponseEntity.ok().body(
                restaurantService.isRestaurantOpen(restaurantId));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PutMapping(RESTAURANT_API)
    public ResponseEntity<Restaurant> putRestaurant(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @Valid @RequestBody Restaurant restaurant) {

        if (!restaurantId.equals(restaurant.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Restaurant id cannot be changed");
        }
        return ResponseEntity.ok().body(
                restaurantService.updateRestaurant(restaurant));
    }

}
