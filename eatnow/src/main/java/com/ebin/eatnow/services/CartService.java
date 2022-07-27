package com.ebin.eatnow.services;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebin.eatnow.dtos.CartDto;
import com.ebin.eatnow.dtos.ItemDto;
import com.ebin.eatnow.entities.Cart;
import com.ebin.eatnow.entities.CartItem;
import com.ebin.eatnow.repositories.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MenuService menuService;

    public CartDto getCart(String userId) {
        return Optional.ofNullable(
                cartRepository.findById(userId))
                .map(
                        (i) -> {
                            return cartToDto(i);
                        })
                .orElse(null);
    }

    public CartDto addToCart(String userId, String restaurantId, int itemIndex, int quantity) {
        ItemDto itemDto = menuService.getItemByRestaurantIdAndIndex(
                restaurantId, itemIndex);
        Cart cart = cartRepository.findById(userId);

        if (cart == null || !cart.getRestaurantId().equals(restaurantId)) {
            cart = new Cart(userId, restaurantId);
        }

        CartItem item = new CartItem(itemDto.getItemIndex(), itemDto.getPrice(),
                quantity);
        cart.add(item);
        cart = cartRepository.save(cart);

        return cartToDto(cart);
    }

    public CartDto updateCart(String userId, int itemIndex, int quantity) {
        Cart cart = Optional.ofNullable(
                cartRepository.findById(userId))
                .orElseThrow(RuntimeException::new);

        cart.update(itemIndex, quantity);
        cart = cartRepository.save(cart);

        return cartToDto(cart);
    }

    public CartDto deleteFromCart(String userId, int itemIndex) {
        Cart cart = Optional.ofNullable(
                cartRepository.findById(userId))
                .orElseThrow(RuntimeException::new);

        cart.delete(itemIndex);
        cart = cartRepository.save(cart);

        return cartToDto(cart);
    }

    public boolean clearCart(String userId) {
        return cartRepository.delete(userId);
    }

    private CartDto cartToDto(Cart cart) {
        CartDto dto = CartDto.builder()
                .userId(cart.getUserId())
                .restaurantId(cart.getRestaurantId())
                .items(cart.getItems().values().stream().map(
                        (i) -> {
                            return new CartDto.CartItem(i.getItemIndex(),
                                    i.getPrice(), i.getQuantity());
                        }).collect(Collectors.toList()))
                .total(cart.getTotal())
                .build();

        return dto;
    }
}
