package com.ebin.eatnow.repositories.dummy;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.ebin.eatnow.entities.Order;
import com.ebin.eatnow.repositories.OrderRepository;

@Repository
public class OrderRepositoryDummy implements OrderRepository {
    private HashMap<UUID, Order> orders = new HashMap<>();

    public Order findById(UUID orderId) {
        return Optional.ofNullable(orders.get(orderId))
                .orElseThrow(RuntimeException::new);
    }

    public List<Order> findByUserId(String userId) {

        return orders.values().stream().filter(
                (i) -> (i.getUserId().equals(userId)))
                .collect(Collectors.toList());
    }

    public List<Order> findByUserIdPaged(String userId, int pageSize, int pageNumber) {

        return findByUserId(userId);
    }

    public List<Order> findByRestaurantId(String restaurantId) {

        return orders.values().stream().filter(
                (i) -> (i.getRestaurantId().equals(restaurantId)))
                .collect(Collectors.toList());
    }

    public List<Order> findByRestaurantIdPaged(String restaurantId, int pageSize, int pageNumber) {

        return findByRestaurantId(restaurantId);
    }

    public List<Order> findByRestaurantIdAndStatus(String restaurantId, String status) {

        Order.Status statusActual = Order.Status.valueOf(status);
        return orders.values().stream().filter(
                (i) -> (i.getRestaurantId().equals(restaurantId)
                        && i.getStatus().equals(statusActual)))
                .collect(Collectors.toList());
    }

    public boolean existsById(UUID orderId) {
        return orders.containsKey(orderId);
    }

    public Order create(Order order) {
        orders.put(order.getId(), order);
        return order;
    }

    public Order update(Order order) {
        orders.put(order.getId(), order);
        return order;
    }

    public boolean delete(UUID orderId) {
        orders.remove(orderId);
        return true;
    }
}
