package com.eatnow.restaurant.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.eatnow.restaurant.dtos.Item;
import com.eatnow.restaurant.dtos.Menu;
import com.eatnow.restaurant.services.MenuService;

@RestController
public class MenuController {

    public static final String MENU_ENDPOINT = "/menu/{restaurantId}";
    public static final String ITEM_ENDPOINT = MENU_ENDPOINT + "/{itemIndex}";
    public static final String ITEM_AVAILABLE_ENDPOINT = MENU_ENDPOINT + "/{itemIndex}/is-available";

    @Autowired
    private MenuService menuService;

    @GetMapping(MENU_ENDPOINT)
    public ResponseEntity<Menu> getMenu(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId) {

        return ResponseEntity.ok().body(
                menuService.getMenuByRestaurantId(restaurantId));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PostMapping(MENU_ENDPOINT)
    public ResponseEntity<Item> postItem(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @Valid @RequestBody Item item) {

        return ResponseEntity.ok().body(
                menuService.createItem(restaurantId, item));
    }

    @GetMapping(ITEM_ENDPOINT)
    public ResponseEntity<Item> getItem(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @PathVariable(name = "itemIndex") @NotNull Integer itemIndex) {

        return ResponseEntity.ok().body(
                menuService.getItemByRestaurantIdAndIndex(restaurantId, itemIndex));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PutMapping(ITEM_ENDPOINT)
    public ResponseEntity<Item> putItem(
            @PathVariable(name = "restaurantId") @NotNull String restaurantId,
            @PathVariable(name = "itemIndex") @NotNull Integer itemIndex,
            @Valid @RequestBody Item item) {

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
}
