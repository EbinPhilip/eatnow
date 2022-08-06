package com.ebin.eatnow.controllers;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ebin.eatnow.dtos.ItemDto;
import com.ebin.eatnow.dtos.MenuDto;
import com.ebin.eatnow.dtos.RestaurantDto;
import com.ebin.eatnow.services.MenuService;
import com.ebin.eatnow.services.RestaurantService;
import com.ebin.eatnow.utils.Location;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class RestaurantController {
    public static final String RESTAURANTS_ENDPOINT = "/restaurant";
    public static final String RESTAURANTS_LOGIN_ENDPOINT = RESTAURANTS_ENDPOINT + "/login";
    public static final String RESTAURANT_API = RESTAURANTS_ENDPOINT + "/{restaurantId}";
    public static final String RESTAURANT_OPEN_API = RESTAURANT_API + "/is-open";

    public static final String MENU_ENDPOINT = "/menu/{restaurantId}";
    public static final String ITEM_ENDPOINT = MENU_ENDPOINT + "/{itemIndex}";
    public static final String ITEM_AVAILABLE_ENDPOINT = MENU_ENDPOINT + "/{itemIndex}/is-available";

    public static final String INTERNAL_FETCH_ITEMS_ENDPOINT = "/internal/serviceable-items";

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @GetMapping(RESTAURANTS_LOGIN_ENDPOINT)
    public ResponseEntity<String> login(
            @RequestParam(name = "restaurant-id") String restaurantId) {

        RestaurantDto restaurant = restaurantService.getRestaurantbyId(restaurantId);
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
    public ResponseEntity<List<RestaurantDto>> getNearbyRestaurants(
            @RequestParam(name = "latitude") Double latitude,
            @RequestParam(name = "longitude") Double longitude) {

        return ResponseEntity.ok().body(
                restaurantService.getRestaurantsNearby(latitude, longitude));
    }

    @GetMapping(RESTAURANT_API)
    public ResponseEntity<RestaurantDto> getRestaurant(
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
    public ResponseEntity<RestaurantDto> putRestaurant(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @Valid @RequestBody RestaurantDto restaurant) {

        if (!restaurantId.equals(restaurant.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Restaurant id cannot be changed");
        }
        return ResponseEntity.ok().body(
                restaurantService.updateRestaurant(restaurant));
    }

    @GetMapping(MENU_ENDPOINT)
    public ResponseEntity<MenuDto> getMenu(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId) {

        return ResponseEntity.ok().body(
                menuService.getMenuByRestaurantId(restaurantId));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PostMapping(MENU_ENDPOINT)
    public ResponseEntity<ItemDto> postItem(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @Valid @RequestBody ItemDto item) {

        return ResponseEntity.ok().body(
                menuService.createItem(restaurantId, item));
    }

    @GetMapping(ITEM_ENDPOINT)
    public ResponseEntity<ItemDto> getItem(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @PathVariable(name = "itemIndex") @NotNull Integer itemIndex) {

        return ResponseEntity.ok().body(
                menuService.getItemByRestaurantIdAndIndex(restaurantId, itemIndex));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PutMapping(ITEM_ENDPOINT)
    public ResponseEntity<ItemDto> putItem(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @PathVariable(name = "itemIndex") @NotNull Integer itemIndex,
            @Valid @RequestBody ItemDto item) {

        return ResponseEntity.ok().body(
                menuService.updateItem(restaurantId, itemIndex, item));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @DeleteMapping(ITEM_ENDPOINT)
    public ResponseEntity<Boolean> deleteItem(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @PathVariable(name = "itemIndex") @NotNull Integer itemIndex) {

        return new ResponseEntity<Boolean>(
                menuService.deleteItem(restaurantId, itemIndex),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @GetMapping(ITEM_AVAILABLE_ENDPOINT)
    public ResponseEntity<Boolean> getItemAvailability(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @PathVariable(name = "itemIndex") @NotNull Integer itemIndex) {

        return new ResponseEntity<Boolean>(
                menuService.getItemAvailability(restaurantId, itemIndex),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PostMapping(ITEM_AVAILABLE_ENDPOINT)
    public ResponseEntity<Boolean> setItemAvailability(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @PathVariable(name = "itemIndex") @NotNull Integer itemIndex,
            @RequestParam(name = "available") @NotNull Boolean available) {

        return new ResponseEntity<Boolean>(
                menuService.setItemAvailability(restaurantId, itemIndex, available),
                HttpStatus.OK);
    }

    @GetMapping(INTERNAL_FETCH_ITEMS_ENDPOINT)
    public ResponseEntity<List<ItemDto>> getServiceableItems(
            @RequestParam(name = "restaurant-id") String restaurantId,
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(name = "indices") Set<Integer> itemIndices) {

        return new ResponseEntity<List<ItemDto>>(menuService
                .checkServiceabilityAndFetchItems(restaurantId,
                        new Location(latitude, longitude),
                        itemIndices),
                HttpStatus.OK);

    }
}
