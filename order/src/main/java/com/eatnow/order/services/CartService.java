package com.eatnow.order.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eatnow.order.dtos.Cart;
import com.eatnow.order.dtos.Order;
import com.eatnow.order.exchanges.OrderRequest;
import com.eatnow.order.services.clients.CartRestClient;

@Service
public class CartService {

    @Autowired
    private CartRestClient restClient;

    public Cart fetchCart(String userId) {

        return restClient.fetchCart(userId);
    }

    public boolean clearCart(String userId) {

        return restClient.clearCart(userId);
    }

    public OrderRequest creatOrderRequestFromCart(String userId, Integer addressIndex) {

        Cart cart = fetchCart(userId);
        if(cart.getItems().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cart is empty");
        }

        return OrderRequest.builder()
                .userId(cart.getUserId())
                .addressIndex(addressIndex)
                .restaurantId(cart.getRestaurantId())
                .items(cart.getItems().stream()
                        .map((i) -> {
                            return new Order.Item(i.getItemIndex(),
                                    i.getPrice(), i.getQuantity());
                        }).collect(Collectors.toList()))
                .total(cart.getTotal())
                .build();
    }
}
