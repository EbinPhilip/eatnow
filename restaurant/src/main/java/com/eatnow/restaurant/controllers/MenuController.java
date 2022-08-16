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
import com.eatnow.restaurant.exchanges.ItemRequest;
import com.eatnow.restaurant.services.MenuService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Menu APIs")
public class MenuController {

    public static final String MENU_ENDPOINT = "/menu/{restaurant-id}";
    public static final String ITEM_ENDPOINT = MENU_ENDPOINT + "/{item-index}";
    public static final String ITEM_AVAILABLE_ENDPOINT = MENU_ENDPOINT + "/{item-index}/is-available";

    @Autowired
    private MenuService menuService;

    @GetMapping(MENU_ENDPOINT)
    @SecurityRequirements()
    @Operation(summary = "Fetch menu", description = "Fetches the menu specified by restaurant-id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content)})
    public ResponseEntity<Menu> getMenu(
            @PathVariable(name = "restaurant-id") @NotNull String restaurantId) {

        return ResponseEntity.ok().body(
                menuService.getMenuByRestaurantId(restaurantId));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PostMapping(MENU_ENDPOINT)
    @Operation(summary = "Add item", description = "Adds a new item to the menu specified by restaurant-id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item added to menu"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content)})
    public ResponseEntity<Item> postItem(
            @PathVariable(name = "restaurant-id") @NotNull String restaurantId,
            @Valid @RequestBody ItemRequest itemRequest) {

        Item item = Item.builder()
                .name(itemRequest.getName())
                .price(itemRequest.getPrice())
                .description(itemRequest.getDescription())
                .tags(itemRequest.getTags())
                .available(itemRequest.getAvailable())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                menuService.createItem(restaurantId, item));
    }

    @GetMapping(ITEM_ENDPOINT)
    @SecurityRequirements()
    @Operation(summary = "Fetch item", description = "Fetches the item specified by restaurant-id and item-index.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Restaurant or item not found", content = @Content)})
    public ResponseEntity<Item> getItem(
            @PathVariable(name = "restaurant-id") @NotNull String restaurantId,
            @PathVariable(name = "item-index") @NotNull Integer itemIndex) {

        return ResponseEntity.ok().body(
                menuService.getItemByRestaurantIdAndIndex(restaurantId, itemIndex));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PutMapping(ITEM_ENDPOINT)
    @Operation(summary = "Update item", description = "Updates the item specified by restaurant-id and item-index.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Restaurant or item not found", content = @Content)})
    public ResponseEntity<Item> putItem(
            @PathVariable(name = "restaurant-id") @NotNull String restaurantId,
            @PathVariable(name = "item-index") @NotNull Integer itemIndex,
            @Valid @RequestBody ItemRequest itemRequest) {

        Item item = Item.builder()
                .name(itemRequest.getName())
                .price(itemRequest.getPrice())
                .description(itemRequest.getDescription())
                .tags(itemRequest.getTags())
                .available(itemRequest.getAvailable())
                .build();
        return ResponseEntity.ok().body(
                menuService.updateItem(restaurantId, itemIndex, item));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @DeleteMapping(ITEM_ENDPOINT)
    @Operation(summary = "Delete item", description = "Deletes the item specified by restaurant-id and item-index.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Restaurant or item not found", content = @Content)})
    public ResponseEntity<Boolean> deleteItem(
            @PathVariable(name = "restaurant-id") @NotNull String restaurantId,
            @PathVariable(name = "item-index") @NotNull Integer itemIndex) {

        return new ResponseEntity<Boolean>(
                menuService.deleteItem(restaurantId, itemIndex),
                HttpStatus.OK);
    }

    @GetMapping(ITEM_AVAILABLE_ENDPOINT)
    @SecurityRequirements()
    @Operation(summary = "Get item availability", description = "Returns availability of the item specified by restaurant-id and item-index.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Restaurant or item not found", content = @Content)})
    public ResponseEntity<Boolean> getItemAvailability(
            @PathVariable(name = "restaurant-id") @NotNull String restaurantId,
            @PathVariable(name = "item-index") @NotNull Integer itemIndex) {

        return new ResponseEntity<Boolean>(
                menuService.getItemAvailability(restaurantId, itemIndex),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @PutMapping(ITEM_AVAILABLE_ENDPOINT)
    @Operation(summary = "Set item availability", description = "Sets availability of the item specified by restaurant-id and item-index. Returns the newly set availability.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Restaurant or item not found", content = @Content)})
    public ResponseEntity<Boolean> setItemAvailability(
            @PathVariable(name = "restaurant-id") @NotNull String restaurantId,
            @PathVariable(name = "item-index") @NotNull Integer itemIndex,
            @RequestParam(name = "available") @NotNull Boolean available) {

        return new ResponseEntity<Boolean>(
                menuService.setItemAvailability(restaurantId, itemIndex, available),
                HttpStatus.OK);
    }
}
