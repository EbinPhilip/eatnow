package com.eatnow.cart.controllers;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.cart.dtos.Cart;
import com.eatnow.cart.services.CartService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Hidden
public class InternalController {

    private static final String INTERNAL_CART_ENDPOINT = "/internal/cart/{user-id}";

    @Autowired
    private CartService cartService;

    @GetMapping(INTERNAL_CART_ENDPOINT)
    public ResponseEntity<Cart> getCart(
            @PathVariable("user-id") @NotNull String userId) {

        return ResponseEntity.ok().body(
                cartService.getCart(userId));
    }

    @DeleteMapping(INTERNAL_CART_ENDPOINT)
    public ResponseEntity<Boolean> clearCart(
            @PathVariable("user-id") @NotNull String userId) {

        return ResponseEntity.ok().body(
                cartService.clearCart(userId));
    }

}
