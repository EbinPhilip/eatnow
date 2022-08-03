package com.ebin.eatnow.repositories.jpaDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ebin.eatnow.entities.Order;

public interface OrderJpaDao extends JpaRepository<Order, UUID> {
    
    public List<Order> findByUserIdAndTimeStampGreaterThan(String userId, LocalDateTime timestamp);

    @Query(value = "SELECT * FROM \"order\" WHERE USER_ID = ?1",
        countQuery = "SELECT count(*) FROM \"order\" WHERE USER_ID = ?1",
        nativeQuery = true)
    public List<Order> findByUserId(String userId, Pageable page);

    public List<Order> findByRestaurantIdAndTimeStampGreaterThan(String restaurantId, LocalDateTime timestamp);

    @Query(value = "SELECT * FROM \"order\" WHERE RESTAURANT_ID = ?1",
        countQuery = "SELECT count(*) FROM \"order\" WHERE RESTAURANT_ID = ?1",
        nativeQuery = true)
    public List<Order> findByRestaurantId(String restaurantId, Pageable page);
}
