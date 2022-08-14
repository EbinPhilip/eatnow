package com.eatnow.order.repositories.dummy;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.eatnow.order.entities.OrderEntity;
import com.eatnow.order.repositories.OrderRepository;

@Repository
public class OrderRepositoryDummy implements OrderRepository {
    private HashMap<UUID, OrderEntity> orders = new HashMap<>();

    public OrderEntity findById(UUID orderId) {
        return Optional.ofNullable(orders.get(orderId))
                .orElseThrow(RuntimeException::new);
    }

    public List<OrderEntity> findByUserId(String userId) {

        return orders.values().stream().filter(
                (i) -> (i.getUserId().equals(userId)))
                .collect(Collectors.toList());
    }

    public List<OrderEntity> findByUserIdPaged(String userId, int pageSize, int pageNumber) {

        return findByUserId(userId);
    }

    public List<OrderEntity> findByRestaurantId(String restaurantId) {

        return orders.values().stream().filter(
                (i) -> (i.getRestaurantId().equals(restaurantId)))
                .collect(Collectors.toList());
    }

    public List<OrderEntity> findByRestaurantIdPaged(String restaurantId, int pageSize, int pageNumber) {

        return findByRestaurantId(restaurantId);
    }

    public List<OrderEntity> findByRestaurantIdAndStatus(String restaurantId, OrderEntity.Status status) {

        OrderEntity.Status statusActual = status;
        return orders.values().stream().filter(
                (i) -> (i.getRestaurantId().equals(restaurantId)
                        && i.getStatus().equals(statusActual)))
                .collect(Collectors.toList());
    }

    public boolean existsById(UUID orderId) {
        return orders.containsKey(orderId);
    }

    public OrderEntity create(OrderEntity order) {
        orders.put(order.getId(), order);
        return order;
    }

    public OrderEntity update(OrderEntity order) {
        orders.put(order.getId(), order);
        return order;
    }
}
