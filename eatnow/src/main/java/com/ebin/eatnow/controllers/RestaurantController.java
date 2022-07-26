package com.ebin.eatnow.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.ebin.eatnow.dtos.RestaurantDto;
import com.ebin.eatnow.services.MenuService;
import com.ebin.eatnow.services.RestaurantService;

@RestController
public class RestaurantController {
    public static final String RESTAURANTS_ENDPOINT = "/restaurant";
    public static final String RESTAURANT_API = RESTAURANTS_ENDPOINT+"/{restaurantId}";

    public static final String MENU_ENDPOINT = "/menu/{restaurantId}";
    public static final String ITEM_ENDPOINT = MENU_ENDPOINT+"/{itemIndex}";
    public static final String ITEM_AVAILABLE_ENDPOINT = MENU_ENDPOINT+"/{itemIndex}/is-available";

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    @GetMapping(RESTAURANTS_ENDPOINT)
    public ResponseEntity<List<RestaurantDto>> getNearbyRestaurants(
        @RequestParam(name="latitude") Double latitude,
        @RequestParam(name="longitude") Double longitude) {
        
        return ResponseEntity.ok().body(
            restaurantService.getRestaurantsNearby(latitude, longitude));
    }

    @GetMapping(RESTAURANT_API)
    public ResponseEntity<RestaurantDto> getRestaurant(
        @PathVariable(name="restaurantId") @NotNull String restaurantId) {
        
        return ResponseEntity.ok().body(
            restaurantService.getRestaurantbyId(restaurantId));
    }

    @PutMapping(RESTAURANT_API)
    public ResponseEntity<RestaurantDto> putRestaurant(
        @PathVariable(name="restaurantId") @NotNull String restaurantId,
        @Valid @RequestBody RestaurantDto restaurant) {

        if (!restaurantId.equals(restaurant.getId()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Restaurant id cannot be changed");
        }
        return ResponseEntity.ok().body(
            restaurantService.updateRestaurant(restaurant));
    }

    @GetMapping(MENU_ENDPOINT)
    public ResponseEntity<List<ItemDto>> getMenu(
        @PathVariable(name="restaurantId") @NotNull String restaurantId) {
        
        return ResponseEntity.ok().body(
            menuService.getItemsByRestaurantId(restaurantId));
    }

    @PostMapping(MENU_ENDPOINT)
    public ResponseEntity<ItemDto> postItem(
        @PathVariable(name="restaurantId") @NotNull String restaurantId,
        @Valid @RequestBody ItemDto item) {
        
        if (!restaurantId.equals(item.getRestaurantId()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                 "Restaurant id provided in the item is different");
        }
        return ResponseEntity.ok().body(
            menuService.createItem(item));
    }

    @GetMapping(ITEM_ENDPOINT)
    public ResponseEntity<ItemDto> getItem(
        @PathVariable(name="restaurantId") @NotNull String restaurantId,
        @PathVariable(name="itemIndex") @NotNull Integer itemIndex) {
        
        return ResponseEntity.ok().body(
            menuService.getItemByRestaurantIdAndIndex(restaurantId, itemIndex));
    }

    @PutMapping(ITEM_ENDPOINT)
    public ResponseEntity<ItemDto> putItem(
        @PathVariable(name="restaurantId") @NotNull String restaurantId,
        @PathVariable(name="itemIndex") @NotNull Integer itemIndex,
        @Valid @RequestBody ItemDto item) {
        
        if (!restaurantId.equals(item.getRestaurantId()) || !itemIndex.equals(item.getItemIndex()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Restaurant id and item index cannot be changed");
        }
        return ResponseEntity.ok().body(
            menuService.updateItem(item));
    }

    @DeleteMapping(ITEM_ENDPOINT)
    public ResponseEntity<Boolean> deleteUserAddress(
        @PathVariable(name="restaurantId") @NotNull String restaurantId,
        @PathVariable(name="itemIndex") @NotNull  Integer itemIndex) {

        return new ResponseEntity<Boolean>(
                menuService.deleteItem(restaurantId, itemIndex),
                HttpStatus.OK);
    }

    @GetMapping(ITEM_AVAILABLE_ENDPOINT)
    public ResponseEntity<Boolean> getItemAvailability(
        @PathVariable(name="restaurantId") @NotNull String restaurantId,
        @PathVariable(name="itemIndex") @NotNull  Integer itemIndex) {
        
        return new ResponseEntity<Boolean>(
                menuService.getItemAvailability(restaurantId, itemIndex),
                HttpStatus.OK);
    }

    @PostMapping(ITEM_AVAILABLE_ENDPOINT)
    public ResponseEntity<Boolean> getItemAvailability(
        @PathVariable(name="restaurantId") @NotNull String restaurantId,
        @PathVariable(name="itemIndex") @NotNull  Integer itemIndex,
        @RequestParam(name="available") @NotNull Boolean available) {
        
        return new ResponseEntity<Boolean>(
                menuService.setItemAvailability(restaurantId, itemIndex, available),
                HttpStatus.OK);
    }
}

