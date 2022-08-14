package com.eatnow.order.repositories.jpaDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eatnow.order.entities.OrderEntity;

public interface OrderJpaDao extends JpaRepository<OrderEntity, UUID> {
    
    public List<OrderEntity> findByUserIdAndTimeStampGreaterThan(String userId, LocalDateTime timestamp);

    @Query(value = "SELECT * FROM \"order\" WHERE USER_ID = ?1",
        countQuery = "SELECT count(*) FROM \"order\" WHERE USER_ID = ?1",
        nativeQuery = true)
    public List<OrderEntity> findByUserId(String userId, Pageable page);

    public List<OrderEntity> findByRestaurantIdAndTimeStampGreaterThan(String restaurantId, LocalDateTime timestamp);

    @Query(value = "SELECT * FROM \"order\" WHERE RESTAURANT_ID = ?1",
        countQuery = "SELECT count(*) FROM \"order\" WHERE RESTAURANT_ID = ?1",
        nativeQuery = true)
    public List<OrderEntity> findByRestaurantId(String restaurantId, Pageable page);
}
