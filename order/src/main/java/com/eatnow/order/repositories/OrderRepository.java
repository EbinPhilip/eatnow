package com.eatnow.order.repositories;

import java.util.List;
import java.util.UUID;

import com.eatnow.order.entities.Order;

public interface OrderRepository {

    public Order findById(UUID orderId);

    public List<Order> findByUserId(String userId);
    public List<Order> findByUserIdPaged(String userId, int pageSize, int pageNumber);

    public List<Order> findByRestaurantId(String restaurantId);
    public List<Order> findByRestaurantIdPaged(String restaurantId, int pageSize, int pageNumber);
    public List<Order> findByRestaurantIdAndStatus(String restaurantId, Order.Status status);

    public boolean existsById(UUID orderId);

    public Order create(Order order);

    public Order update(Order order);
}
