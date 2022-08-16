package com.eatnow.cart.services;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eatnow.cart.dtos.Cart;
import com.eatnow.cart.dtos.ItemDto;
import com.eatnow.cart.entities.CartEntity;
import com.eatnow.cart.entities.CartItemEntity;
import com.eatnow.cart.repositories.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MenuService menuService;

    public Cart getCart(String userId) {
        return Optional.ofNullable(
                cartRepository.findById(userId))
                .map(
                        (i) -> {
                            return cartToDto(i);
                        })
                .orElse(null);
    }

    public Cart addToCart(String userId, String restaurantId, int itemIndex, int quantity) {

        CartEntity cart = cartRepository.findById(userId);
        if (quantity == 0) {
            return (cart == null) ? null : cartToDto(cart);
        }

        ItemDto itemDto = menuService.getItemByRestaurantIdAndIndex(
                restaurantId, itemIndex);

        if (cart == null || !cart.getRestaurantId().equals(restaurantId)) {
            cart = new CartEntity(userId, restaurantId, itemDto.getRestaurantName());
        }

        CartItemEntity item = new CartItemEntity(itemDto.getName(),
                itemDto.getItemIndex(), itemDto.getPrice(), quantity);
        cart.add(item);
        cart = cartRepository.save(cart);

        return cartToDto(cart);
    }

    public Cart updateCart(String userId, int itemIndex, int quantity) {

        CartEntity cart = Optional.ofNullable(
                cartRepository.findById(userId))
                .orElseThrow(() -> (new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "cart is empty")));

        if (quantity == 0) {
            cart.delete(itemIndex);
        } else {
            cart.update(itemIndex, quantity);
        }
        cart = cartRepository.save(cart);

        return cartToDto(cart);
    }

    public Cart deleteFromCart(String userId, int itemIndex) {
        CartEntity cart = Optional.ofNullable(
                cartRepository.findById(userId))
                .orElseThrow(() -> (new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "cart is empty")));

        cart.delete(itemIndex);
        cart = cartRepository.save(cart);

        return cartToDto(cart);
    }

    public boolean clearCart(String userId) {
        return cartRepository.delete(userId);
    }

    private Cart cartToDto(CartEntity cart) {
        Cart dto = Cart.builder()
                .userId(cart.getUserId())
                .restaurantId(cart.getRestaurantId())
                .restaurantName(cart.getRestaurantName())
                .items(cart.getItems().values().stream().map(
                        (i) -> {
                            return new Cart.CartItem(i.getName(), i.getItemIndex(),
                                    i.getPrice(), i.getQuantity());
                        }).collect(Collectors.toList()))
                .total(cart.getTotal())
                .build();

        return dto;
    }
}
