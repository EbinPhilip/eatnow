package com.eatnow.cart.controllers;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.cart.dtos.CartDto;
import com.eatnow.cart.services.CartService;

@RestController
public class CartController {
        public static final String CART_ENDPOINT = "/cart";
        public static final String CART_API = CART_ENDPOINT + "/{userId}";

        @Autowired
        private CartService cartService;

        @PreAuthorize("hasRole('ROLE_USER') and" +
                        "#userId == authentication.principal.username")
        @GetMapping(CART_API)
        public ResponseEntity<CartDto> getCart(
                        @PathVariable("userId") @NotNull String userId) {

                return ResponseEntity.ok().body(
                                cartService.getCart(userId));
        }

        @PreAuthorize("hasRole('ROLE_USER') and" +
                        "#userId == authentication.principal.username")
        @PostMapping(CART_API)
        public ResponseEntity<CartDto> postToCart(
                        @PathVariable("userId") @NotNull String userId,
                        @RequestParam("restaurant-id") @NotNull String restaurantId,
                        @RequestParam("item-index") @NotNull Integer itemIndex,
                        @RequestParam("quantity") @NotNull Integer quantity) {

                return ResponseEntity.ok().body(
                                cartService.addToCart(userId, restaurantId, itemIndex, quantity));
        }

        @PreAuthorize("hasRole('ROLE_USER') and" +
                        "#userId == authentication.principal.username")
        @DeleteMapping(CART_API)
        public ResponseEntity<Boolean> clearCart(
                        @PathVariable("userId") @NotNull String userId) {

                return ResponseEntity.ok().body(
                                cartService.clearCart(userId));
        }

        @PreAuthorize("hasRole('ROLE_USER') and" +
                        "#userId == authentication.principal.username")
        @PutMapping(CART_API + "/{itemIndex}")
        public ResponseEntity<CartDto> updateCart(
                        @PathVariable("userId") @NotNull String userId,
                        @PathVariable("itemIndex") @NotNull Integer itemIndex,
                        @RequestParam("quantity") @NotNull Integer quantity) {

                return ResponseEntity.ok().body(
                                cartService.updateCart(userId, itemIndex, quantity));
        }

        @PreAuthorize("hasRole('ROLE_USER') and" +
                        "#userId == authentication.principal.username")
        @DeleteMapping(CART_API + "/{itemIndex}")
        public ResponseEntity<CartDto> deleteFromCart(
                        @PathVariable("userId") @NotNull String userId,
                        @PathVariable("itemIndex") @NotNull Integer itemIndex) {

                return ResponseEntity.ok().body(
                                cartService.deleteFromCart(userId, itemIndex));
        }
}
