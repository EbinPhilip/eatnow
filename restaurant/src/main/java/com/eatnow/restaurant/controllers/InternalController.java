package com.eatnow.restaurant.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.restaurant.dtos.Item;
import com.eatnow.restaurant.services.MenuService;
import com.eatnow.restaurant.utils.Location;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Hidden
public class InternalController {

    public static final String INTERNAL_FETCH_ITEMS_ENDPOINT = "/internal/serviceable-items";

    @Autowired
    private MenuService menuService;

    @GetMapping(INTERNAL_FETCH_ITEMS_ENDPOINT)
    public ResponseEntity<List<Item>> getServiceableItems(
            @RequestParam(name = "restaurant-id") String restaurantId,
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(name = "indices") Set<Integer> itemIndices) {

        return new ResponseEntity<List<Item>>(menuService
                .checkServiceabilityAndFetchItems(restaurantId,
                        new Location(latitude, longitude),
                        itemIndices),
                HttpStatus.OK);
    }
}
