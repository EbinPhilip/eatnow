package com.eatnow.order.repositories;

import java.util.List;
import java.util.UUID;

import com.eatnow.order.entities.OrderEntity;

public interface OrderRepository {

    public OrderEntity findById(UUID orderId);

    public List<OrderEntity> findByUserId(String userId);
    public List<OrderEntity> findByUserIdPaged(String userId, int pageSize, int pageNumber);

    public List<OrderEntity> findByRestaurantId(String restaurantId);
    public List<OrderEntity> findByRestaurantIdPaged(String restaurantId, int pageSize, int pageNumber);
    public List<OrderEntity> findByRestaurantIdAndStatus(String restaurantId, OrderEntity.Status status);

    public boolean existsById(UUID orderId);

    public OrderEntity create(OrderEntity order);

    public OrderEntity update(OrderEntity order);
}
