package com.eatnow.order.repositories.jpaDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eatnow.order.entities.OrderEntity;

public interface OrderJpaDao extends JpaRepository<OrderEntity, UUID> {
    
    public List<OrderEntity> findByUserIdAndTimeStampGreaterThan(String userId, LocalDateTime timestamp);

    public Page<OrderEntity> findByUserIdOrderByTimeStampDesc(String userId, Pageable page);

    public List<OrderEntity> findByRestaurantIdAndTimeStampGreaterThan(String restaurantId, LocalDateTime timestamp);

    public Page<OrderEntity> findByRestaurantIdOrderByTimeStampDesc(String restaurantId, Pageable page);
}
