package com.eatnow.cart.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.cart.dtos.Cart;
import com.eatnow.cart.services.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Cart APIs")
public class CartController {
    public static final String CART_ENDPOINT = "/cart";
    public static final String CART_API = CART_ENDPOINT + "/{user-id}";

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @GetMapping(CART_API)
    @Operation(summary = "Fetch cart", description = "Fetches the shopping cart of the user specified by user-id. Returns empty response body if cart doesn't exist.")
    public ResponseEntity<Cart> getCart(
            @PathVariable("user-id") @NotNull String userId) {

        return ResponseEntity.ok().body(
                cartService.getCart(userId));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @PostMapping(CART_API)
    @Operation(summary = "Add to cart", description = "Adds item specified by restaurant-id, item-index and qunatity, to the user's cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added to cart"),
            @ApiResponse(responseCode = "404", description = "restaurant or item not found", content = @Content) })
    public ResponseEntity<Cart> postToCart(
            @PathVariable("user-id") @NotNull String userId,
            @RequestParam("restaurant-id") @NotNull String restaurantId,
            @RequestParam("item-index") @NotNull Integer itemIndex,
            @RequestParam("quantity") @NotNull Integer quantity) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                cartService.addToCart(userId, restaurantId, itemIndex, quantity));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @DeleteMapping(CART_API)
    @Operation(summary = "Clear cart", description = "Removes all items present in the shopping cart of the user specified by user-id.")
    public ResponseEntity<Boolean> clearCart(
            @PathVariable("user-id") @NotNull String userId) {

        return ResponseEntity.ok().body(
                cartService.clearCart(userId));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @PutMapping(CART_API + "/{item-index}")
    @Operation(summary = "Update item quantity", description = "Updates the quantity of the specified item in the user's cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Incorrect request structure or cart is empty", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item is not present in the cart", content = @Content) })
    public ResponseEntity<Cart> updateCart(
            @PathVariable("user-id") @NotNull String userId,
            @PathVariable("item-index") @NotNull Integer itemIndex,
            @RequestParam("quantity") @NotNull Integer quantity) {

        return ResponseEntity.ok().body(
                cartService.updateCart(userId, itemIndex, quantity));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @DeleteMapping(CART_API + "/{item-index}")
    @Operation(summary = "Remove item", description = "Removes the specified item from the user's cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Incorrect request structure or cart is empty", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item is not present in the cart", content = @Content) })
    public ResponseEntity<Cart> deleteFromCart(
            @PathVariable("user-id") @NotNull String userId,
            @PathVariable("item-index") @NotNull Integer itemIndex) {

        return ResponseEntity.ok().body(
                cartService.deleteFromCart(userId, itemIndex));
    }
}
