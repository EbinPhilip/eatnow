package com.ebin.eatnow.repositories;

import java.util.List;

import com.ebin.eatnow.entities.Order;

public interface OrderRepository {
    public Order findById(String orderId);

    public List<Order> findByUserId(String userId);
    public List<Order> findByRestaurantId(String restaurantId);
    public List<Order> findByRestaurantIdAndStatus(String restaurantId, String status);

    public boolean existsById(String orderId);

    public Order create(Order order);

    public Order update(Order order);

    public boolean delete(String orderId);
}
