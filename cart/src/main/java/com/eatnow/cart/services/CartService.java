package  com.eatnow.cart.services;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import  com.eatnow.cart.dtos.Cart;
import  com.eatnow.cart.dtos.ItemDto;
import  com.eatnow.cart.entities.CartEntity;
import  com.eatnow.cart.entities.CartItemEntity;
import  com.eatnow.cart.repositories.CartRepository;

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
        ItemDto itemDto = menuService.getItemByRestaurantIdAndIndex(
                restaurantId, itemIndex);
        CartEntity cart = cartRepository.findById(userId);

        if (cart == null || !cart.getRestaurantId().equals(restaurantId)) {
            cart = new CartEntity(userId, restaurantId);
        }

        CartItemEntity item = new CartItemEntity(itemDto.getItemIndex(), itemDto.getPrice(),
                quantity);
        cart.add(item);
        cart = cartRepository.save(cart);

        return cartToDto(cart);
    }

    public Cart updateCart(String userId, int itemIndex, int quantity) {
        CartEntity cart = Optional.ofNullable(
                cartRepository.findById(userId))
                .orElseThrow(RuntimeException::new);

        cart.update(itemIndex, quantity);
        cart = cartRepository.save(cart);

        return cartToDto(cart);
    }

    public Cart deleteFromCart(String userId, int itemIndex) {
        CartEntity cart = Optional.ofNullable(
                cartRepository.findById(userId))
                .orElseThrow(RuntimeException::new);

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
                .items(cart.getItems().values().stream().map(
                        (i) -> {
                            return new Cart.CartItem(i.getItemIndex(),
                                    i.getPrice(), i.getQuantity());
                        }).collect(Collectors.toList()))
                .total(cart.getTotal())
                .build();

        return dto;
    }
}
