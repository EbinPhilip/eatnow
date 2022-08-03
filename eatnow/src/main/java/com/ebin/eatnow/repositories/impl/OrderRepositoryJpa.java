package com.ebin.eatnow.repositories.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;

import com.ebin.eatnow.entities.Order;
import com.ebin.eatnow.repositories.OrderRepository;
import com.ebin.eatnow.repositories.jpaDao.OrderJpaDao;

@Repository
@Primary
public class OrderRepositoryJpa implements OrderRepository {

    @Autowired
    OrderJpaDao dao;

    public Order findById(UUID orderId) {

        return dao.findById(orderId)
                .orElseThrow(RuntimeException::new);
    }

    public List<Order> findByUserId(String userId) {

        Pageable page = PageRequest.of(0, 10);
        return dao.findByUserId(userId, page);
    }

    public List<Order> findByUserIdPaged(String userId, int pageSize, int pageNumber) {

        Pageable page = PageRequest.of(pageNumber, pageSize);
        return dao.findByUserId(userId, page);
    }

    public List<Order> findByRestaurantId(String restaurantId) {

        LocalDateTime stamp = LocalDateTime.now().minusDays(1);
        return dao.findByRestaurantIdAndTimeStampGreaterThan(restaurantId, stamp);
    }

    public List<Order> findByRestaurantIdPaged(String restaurantId, int pageSize, int pageNumber) {

        Pageable page = PageRequest.of(pageNumber, pageSize);
        return dao.findByRestaurantId(restaurantId, page);
    }

    public List<Order> findByRestaurantIdAndStatus(String restaurantId, String status) {

        return findByRestaurantId(restaurantId)
                .stream()
                .filter((i)->(i.getStatus().toString() == status))
                .collect(Collectors.toList());
    }

    public boolean existsById(UUID orderId) {

        return dao.existsById(orderId);
    }

    public Order create(Order order) {

        return dao.save(order);
    }

    @Transactional
    public Order update(Order order) {

        findById(order.getId());
        return dao.save(order);
    }

    public boolean delete(UUID orderId) {

        dao.deleteById(orderId);
        return true;
    }
}
